package org.andromda.utils.beautifier.core;

/**
 * Copyright 2008 hybrid labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.language.antlr.JavaNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple oAW beautifier implementation based on Jalopy and the antlr JavaNode. The
 * implementation substitutes fully qualified class names (as long as there is
 * no conflict) with the short name and the according import statement.
 *
 * @author Karsten Klein, hybrid labs;
 * @author Plushnikov Michail
 */
public class JavaImportBeautifierImpl extends JavaBeautifier
{
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(JavaImportBeautifierImpl.class);

    private static final boolean DEBUG = false;

    private static final String DEFAULT_PACKAGE = "";
    private static final String JAVA_LANG_PACKAGE = "java.lang";

    private static final String IMPORT = "import ";

    private static Set<Integer> startSequenceTypeSet = new HashSet<Integer>();
    private static Set<Integer> terminateSequenceTypeSet = new HashSet<Integer>();

    static
    {
        startSequenceTypeSet.add(PACKAGE);
        startSequenceTypeSet.add(IMPORT_STATEMENT);
        startSequenceTypeSet.add(GENERIC_UPPER_BOUNDS);
        startSequenceTypeSet.add(EXTENDS_CLAUSE);
        startSequenceTypeSet.add(IMPLEMENTS_CLAUSE);
        startSequenceTypeSet.add(TYPE);
        startSequenceTypeSet.add(EXPRESSION);
        startSequenceTypeSet.add(NEW);
        startSequenceTypeSet.add(55);
        startSequenceTypeSet.add(ANNOTATION);
        startSequenceTypeSet.add(ANNOTATION_VALUE);
        startSequenceTypeSet.add(CAST);
        startSequenceTypeSet.add(19);
        startSequenceTypeSet.add(AT);
        startSequenceTypeSet.add(74); // semicolon
        startSequenceTypeSet.add(STATIC_IMPORT_STATEMENT);
        startSequenceTypeSet.add(80);

        // these terminate a sequence AND inherit the scope of the current (separator types)
        terminateSequenceTypeSet.add(10);
        terminateSequenceTypeSet.add(100);
        terminateSequenceTypeSet.add(109);
        terminateSequenceTypeSet.add(80);
        terminateSequenceTypeSet.add(39);
        terminateSequenceTypeSet.add(11);
        terminateSequenceTypeSet.add(45);
    }

    private boolean isOrganizeImports = true;
    private boolean isStrict = true;
    private boolean isFormat = true;

    private Map<Integer, TypeReplacementStrategy> strategyMap = new HashMap<Integer, TypeReplacementStrategy>();

    /**
     *
     */
    public JavaImportBeautifierImpl()
    {
        // populate strategy map
        strategyMap.put(NEW, new DefaultTypeReplacementStrategy("[\\,|\\<|\\)|\\[|\\<|\\(|\\s]"));
        strategyMap.put(GENERIC_UPPER_BOUNDS, new DefaultTypeReplacementStrategy("[\\(|\\,|\\<|\\[|\\,|\\>|\\s]"));
        strategyMap.put(EXTENDS_CLAUSE, new DefaultTypeReplacementStrategy("[\\(|\\>|\\<|\\[|\\,|\\;|\\{\\s]"));
        strategyMap.put(IMPLEMENTS_CLAUSE, new DefaultTypeReplacementStrategy("[\\(|\\>|\\<|\\[|\\,|\\;|\\{|\\s]"));

        strategyMap.put(TYPE, new DefaultTypeReplacementStrategy("[\\(|\\,|\\>|\\)\\[\\<|\\;|\\s|\\.]"));
        strategyMap.put(35, new DefaultTypeReplacementStrategy("[\\(|\\,|\\>|\\)\\[\\<|\\;|\\s|\\.]"));
        strategyMap.put(EXPRESSION, new DefaultTypeReplacementStrategy("[\\(|\\,|\\>|\\<|\\)|\\.|\\s*]"));
        strategyMap.put(ANNOTATION, new DefaultTypeReplacementStrategy("[\\(|\\>|\\<|\\[\\,|\\;|\\{|\\s]"));
        strategyMap.put(AT, new DefaultTypeReplacementStrategy("[\\(|\\>|\\<|\\[\\,|\\;|\\{|\\s]"));
        strategyMap.put(CAST, new DefaultTypeReplacementStrategy("[\\(|\\)|\\>|\\<|\\[\\,|\\;|\\{|\\s|\\.]"));
        strategyMap.put(ANNOTATION_VALUE, new DefaultTypeReplacementStrategy("[\\(|\\>|\\<|\\[|\\,|\\;|\\{|\\s|\\.]"));
    }

    /**
     * @return isFormat
     */
    public boolean isFormat()
    {
        return isFormat;
    }

    /**
     * @param format
     */
    public void setFormat(boolean format)
    {
        isFormat = format;
    }

    /**
     * @return isOrganizeImports
     */
    public boolean isOrganizeImports()
    {
        return isOrganizeImports;
    }

    /**
     * @param isOrganizeImports
     */
    public void setOrganizeImports(boolean isOrganizeImports)
    {
        this.isOrganizeImports = isOrganizeImports;
    }

    public boolean isStrict()
    {
        return isStrict;
    }

    public void setStrict(boolean strict)
    {
        isStrict = strict;
    }

    /**
     * Beautifies the source file and writes the result to the target file.
     * Source and target may be identical.
     *
     * @param sourceFile the source file
     * @param targetFile the output file
     * @throws java.io.IOException on IOErrors
     */
    public void beautify(File sourceFile, File targetFile) throws IOException
    {
        final String lSource = FileUtils.readFileToString(sourceFile);

        String output = beautify(lSource);

        String targetPath = sourceFile.getAbsolutePath();
        if (targetFile != null)
        {
            targetPath = targetFile.getAbsolutePath();
        }

        FileWriter writer = new FileWriter(new File(targetPath));
        writer.write(output);
        writer.close();
    }

    /**
     * Beautify and Organize Imports on the source file.
     *
     * @see org.andromda.utils.beautifier.core.Beautifier#beautify(java.lang.String)
     */
    public String beautify(String pSource)
    {
        String result = pSource;
        File tempFile = null;
        try
        {
            tempFile = File.createTempFile("andromda-beautifier", "java");
            if (isOrganizeImports())
            {
                result = organizeImports(result, tempFile);
            }
            if (isFormat())
            {
                result = format(result, tempFile);
            }
            // Note: Jalopy JavaRecognizer.parse() does System.err.println and does not rethrow exception
        }
        catch (Exception e)
        {
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Error during beautification. Content:\r\n" + pSource.substring(0, Math.min(160, pSource.length())) + "...", e);
            }
            LOGGER.warn("Error during beautification. Source will not be beautified!");
        }
        catch (Error e)
        {
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Error during beautification. Content:\r\n" + pSource.substring(0, Math.min(160, pSource.length())) + "...", e);
            }
            LOGGER.warn("Error during beautification. Source will not be beautified!");
        }
        finally
        {
            if (null != tempFile)
            {
                tempFile.delete();
            }
        }
        return result;
    }

    @SuppressWarnings("null")
    private String organizeImports(final String pSource, File pTempFile)
    {
        // create formatter context for this pTempFile
        BeautifierContext formatterContext = new BeautifierContext();

        // use formatter context to traverse the abstract syntax tree
        Jalopy jalopy = createJavaNode(pSource, pTempFile);
        JavaNode node = jalopy.parse();
        traverseAst(node, formatterContext, 0);

        Collection<TypeContext> sequences = formatterContext.getSequences();

        // dump all detected sequences to system out in DEBUG mode
        if (DEBUG)
        {
            printSequences(sequences);
        }

        // extract package from ast
        String currentPackage = DEFAULT_PACKAGE;
        for (TypeContext typeContext : sequences)
        {
            if (PACKAGE_ANNOTATION == typeContext.getType())
            {
                currentPackage = typeContext.getQualifiedName();
                break;
            }
        }

        // determine absolute position of package declaration
        int packageEndPos = findPositionInCharacterSequence(pSource,
                formatterContext.getPackageLine(), formatterContext.getPackageEnd());

        // determine absolute position of import declaration
        int importEndPos = findPositionInCharacterSequence(pSource,
                formatterContext.getImportEndLine(), formatterContext.getImportEndColumn());

        // split the pTempFile in header (including the package declaration) ...
        if (DEFAULT_PACKAGE.equals(currentPackage))
        {
            packageEndPos = 0;
        }

        if (DEBUG)
        {
            System.out.println("Package: " + currentPackage + '[' + packageEndPos + ']');
            System.out.println("Imports end at: " + importEndPos);
        }

        if (-1 == importEndPos)
        {
            importEndPos = packageEndPos;
        }

        String oldImports = pSource.substring(packageEndPos, importEndPos);

        final StringBuilder result = new StringBuilder(pSource.length());
        // do not organize anything if there is a wildcard import - this could lead to ambiguity
        if (!isStrict || !oldImports.contains(".*"))
        {
            // and the body part (imports, comments, classes/interfaces)
            String body = pSource.substring(importEndPos, pSource.length());

            // extract the detected import sequences
            Collection<String> importTypes = extractImports(sequences);

            // set for monitoring the already replaced types
            Set<String> replacedSet = new TreeSet<String>();
            // add all locally referenced types
            for (TypeContext typeContext : sequences)
            {
                if (19 == typeContext.getType())
                {
                    if (null == currentPackage)
                    {
                        importTypes.add(typeContext.getQualifiedName());
                    }
                    else
                    {
                        importTypes.add(currentPackage + '.' + typeContext.getQualifiedName());
                    }
                }
            }
            // add some of basic java.lang types
            initializeJavaLang(importTypes);

            body = replaceFullQualifiedClassNames(body, sequences, importTypes, replacedSet);

            // collect all * imports
            Collection<String> lStarImports = new HashSet<String>();
            for (String pImportType : importTypes)
            {
                if (pImportType.endsWith(".*"))
                {
                    lStarImports.add(pImportType);
                }
            }
            for (String pImportType : replacedSet)
            {
                if (pImportType.endsWith(".*"))
                {
                    lStarImports.add(pImportType);
                }
            }

            // Start output
            result.append(pSource.subSequence(0, packageEndPos));

            // Check for comments in the import sections
            if (oldImports.length() > 0 && (oldImports.contains("//") || oldImports.contains("/*") || oldImports.contains("*/")))
            {
                // append unchanged old import section
                result.append(oldImports);
                // remove duplicated imports
                replacedSet.removeAll(importTypes);
                // output generated imports
                appendImports(result, replacedSet, currentPackage, lStarImports);
            }
            else
            {
                // merge all imports
                importTypes.addAll(replacedSet);
                // output all imports
                appendImports(result, importTypes, currentPackage, lStarImports);
            }

            // supplement the rest of the source
            result.append(body);
        }
        else
        {
            LOGGER.info("Did not organize imports - file contains wildcard imports.");
            result.append(pSource);
        }

        // cleanup
        jalopy.reset();
        // Hack to prevent memory leak
        clearObject(jalopy);

        return result.toString();
    }

    private void appendImports(StringBuilder pBuffer, Collection<String> pImportTypes, final String pPackage, final Collection<String> pStarImports)
    {
        boolean needNewLine = false;

        for (String pImportType : pImportTypes)
        {
            String typePackage = extractPackage(pImportType);

            // make sure the import is not redundant, because :
            //  - it is part of the current package
            //  - it is java.lang import (automatically imported)
            //  - there is * import from the same package already
            if (StringUtils.isNotBlank(typePackage) && !typePackage.equals(pPackage)
                    && !JAVA_LANG_PACKAGE.equals(typePackage) &&
                    (pImportType.endsWith(".*") || !pStarImports.contains(typePackage + ".*")))
            {
                pBuffer.append(IMPORT).append(pImportType).append(";\r\n");
                needNewLine = true;
            }
        }

        if (needNewLine)
        {
            // append new line
            pBuffer.append("\r\n");
        }
    }

    /**
     * Hack to prevent memory leaks
     *
     * @param jalopy
     */
    private void clearObject(Jalopy jalopy)
    {
        Class clazz = jalopy.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                field.set(jalopy, null);
            }
            catch (IllegalArgumentException ignore)
            {
            }
            catch (IllegalAccessException ignore)
            {
            }
        }
    }

    private String replaceFullQualifiedClassNames(String body, Collection<TypeContext> sequences,
                                                  Collection<String> importTypes,
                                                  Collection<String> replacedSet)
    {
        final Set<String> conflictCache = new HashSet<String>();

        for (TypeContext typeContext : sequences)
        {
            String type = typeContext.getQualifiedName();

            if (!replacedSet.contains(type))
            {
                if (typeContext.isValidType())
                {
                    // ensure there are no conflicts
                    if (!isConflict(type, importTypes, replacedSet, conflictCache))
                    {
                        // access the replacement strategy
                        TypeReplacementStrategy strategy = strategyMap.get(typeContext.getType());

                        if (null != strategy)
                        {
                            Pattern pattern = Pattern.compile(strategy.composeMatch(type));
                            Matcher matcher = pattern.matcher(body);

                            // check whether the pattern was matched
                            if (matcher.find())
                            {
                                final String replacement = strategy.composeReplace(type);

                                // match and replace the type by the short form (in the sub string)
                                body = matcher.replaceAll(replacement);

                                // mark type as replaced
                                replacedSet.add(type);
                            }
                        }
                    }
                }
            }
        }
        return body;
    }

    private void initializeJavaLang(Collection<String> pCache)
    {
        // add default java lang import
        pCache.add("java.lang.*");
        pCache.add("java.lang.Short");
        pCache.add("java.lang.Integer");
        pCache.add("java.lang.Long");
        pCache.add("java.lang.Float");
        pCache.add("java.lang.Double");
        pCache.add("java.lang.String");
        pCache.add("java.lang.Character");
        pCache.add("java.lang.Boolean");
        pCache.add("java.lang.Byte");
        pCache.add("java.lang.Number");
        pCache.add("java.lang.Exeption");
        pCache.add("java.lang.StringBuilder");
        pCache.add("java.lang.StringBuffer");
    }

    private Collection<String> extractImports(Collection<TypeContext> sequences)
    {
        Collection<String> importTypes = new TreeSet<String>();

        for (TypeContext typeContext : sequences)
        {
            if (IMPORT_STATEMENT == typeContext.getType())
            {
                importTypes.add(typeContext.getQualifiedName());
            }
            else if (STATIC_IMPORT_STATEMENT == typeContext.getType())
            {
                final StringBuffer sb = new StringBuffer(100);
                sb.append("static ");
                sb.append(typeContext.getQualifiedName());
                importTypes.add(sb.toString());
            }
        }
        return importTypes;
    }

    private void printSequences(Collection<TypeContext> sequences)
    {
        for (TypeContext typeContext : sequences)
        {
            System.out.println(typeContext);
        }
    }

    private boolean isConflict(String type, Collection<String> importList,
                               Collection<String> replacedSet, Collection<String> conflictCache)
    {
        if (JAVA_LANG_PACKAGE.equals(extractPackage(type)))
        {
            return false;
        }

        if (isConflict(type, replacedSet, conflictCache))
        {
            return true;
        }

        if (isConflict(type, importList, conflictCache))
        {
            return true;
        }
        return false;
    }

    private boolean isConflict(String type, Collection<String> testSet, Collection<String> conflictCache)
    {
        if (testSet.contains(type))
        {
            return false;
        }

        if (conflictCache.contains(type))
        {
            return true;
        }

        for (String importType : testSet)
        {
            if (!importType.endsWith("*"))
            {
                if (!type.equals(importType))
                {
                    String className = importType.substring(importType.lastIndexOf('.'));
                    if (type.endsWith(className) && !importType.startsWith("static "))
                    {
                        conflictCache.add(type);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String extractPackage(String pImportType)
    {
        final int index = pImportType.lastIndexOf('.');
        String typePackage = DEFAULT_PACKAGE;
        if (-1 < index)
        {
            typePackage = pImportType.substring(0, index);
        }
        return typePackage;
    }

    private int traverseAst(JavaNode ast, BeautifierContext context, int depth)
    {
        // print this node
        if (DEBUG)
        {
            preparePrintln(depth);
            System.out.println(ast);
        }

        if (context.isProcessed(ast))
        {
            return -1;
        }

        context.preProcess(ast);

        // check whether this subtree is to be ignored completely
        Integer astType = ast.getType();

        if (terminateSequenceTypeSet.contains(astType))
        {
            context.terminateCurrentSequence(ast, context.getCurrentTypeContext().getType());
        }
        else if (startSequenceTypeSet.contains(astType))
        {
            context.terminateCurrentSequence(ast, -1);
        }

        if (DEBUG)
        {
            System.out.println("TRAVERSING: " + ast.getType());
        }

        // traverse all children
        JavaNode child = (JavaNode) ast.getFirstChild();
        if (null != child)
        {
            traverseAst(child, context, depth + 1);
        }

        // print if a type was detected
        if (79 == ast.getType())
        {
            context.addToCurrentTypeContext(ast);
            if (DEBUG)
            {
                preparePrintln(depth);
                System.out.println(context.getCurrentTypeContext());
            }
        }

        // continue visiting the tree (traverse siblings)
        JavaNode next = (JavaNode) ast.getNextSibling();
        while (null != next)
        {
            traverseAst(next, context, depth);
            next = (JavaNode) next.getNextSibling();
        }

        context.postProcess(ast);
        return -1;
    }

    private void preparePrintln(int depth)
    {
        for (int i = 0; i < depth; i++)
        {
            System.out.print("    ");
        }
    }

}