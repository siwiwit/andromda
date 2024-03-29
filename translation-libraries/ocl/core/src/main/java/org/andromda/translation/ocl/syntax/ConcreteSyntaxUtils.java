package org.andromda.translation.ocl.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.translation.ocl.node.AActualParameterList;
import org.andromda.translation.ocl.node.ACommaExpression;
import org.andromda.translation.ocl.node.AFeatureCall;
import org.andromda.translation.ocl.node.AFeatureCallParameters;
import org.andromda.translation.ocl.node.AOperation;
import org.andromda.translation.ocl.node.APropertyCallExpression;
import org.andromda.translation.ocl.node.ARelationalExpression;
import org.andromda.translation.ocl.node.ARelationalExpressionTail;
import org.andromda.translation.ocl.node.AStandardDeclarator;
import org.andromda.translation.ocl.node.ATypeDeclaration;
import org.andromda.translation.ocl.node.AVariableDeclaration;
import org.andromda.translation.ocl.node.AVariableDeclarationList;
import org.andromda.translation.ocl.node.AVariableDeclarationListTail;
import org.andromda.translation.ocl.node.PActualParameterList;
import org.andromda.translation.ocl.node.PEqualExpression;
import org.andromda.translation.ocl.node.PFeatureCallParameters;
import org.andromda.translation.ocl.node.POperation;
import org.andromda.translation.ocl.node.PRelationalExpression;
import org.andromda.translation.ocl.node.PVariableDeclaration;
import org.andromda.translation.ocl.node.PVariableDeclarationList;
import org.andromda.translation.ocl.node.TName;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Contains some utilities for concrete syntax value retrieval.
 *
 * @author Chad Brandon
 */
public class ConcreteSyntaxUtils
{
    private static final Logger logger = Logger.getLogger(ConcreteSyntaxUtils.class);

    /**
     * Iterates through the passed in list and concatenates all the values of objects toString value to a StringBuffer and
     * returns the StringBuffer.
     *
     * @param list the List of objects to concatenate.
     * @return StringBuffer the concatenated contents of the list.
     */
    public static StringBuffer concatContents(List list)
    {
        StringBuffer name = new StringBuffer();
        if (list != null)
        {
            for (Object object : list)
            {
                String value = ObjectUtils.toString(object);
                name.append(value);
            }
        }
        return name;
    }

    /**
     * Converts the passed <code>operation</code> to an instance of <code>Operation</code> for the passed in operation.
     *
     * @param operation
     * @return VariableDeclarations
     */
    public static OperationDeclaration getOperationDeclaration(POperation operation)
    {
        ExceptionUtils.checkNull("operation", operation);

        OperationDeclaration operationDeclaration = null;

        AOperation op = (AOperation) operation;

        ATypeDeclaration typeDeclaration = (ATypeDeclaration) op.getReturnTypeDeclaration();
        String returnType = null;
        if (typeDeclaration != null)
        {
            returnType = ObjectUtils.toString(typeDeclaration.getType());
        }

        operationDeclaration = new OperationDeclarationImpl(ObjectUtils.toString(op.getName()), returnType, ConcreteSyntaxUtils.getVariableDeclarations(
                operation));
        return operationDeclaration;
    }

    /**
     * Retrieves all the variable declarations for the passed in <code>operation</code>.
     *
     * @param operation the operation for which to retrieve the variable declarations.
     * @return VariableDeclaration[]
     */
    public static VariableDeclaration[] getVariableDeclarations(POperation operation)
    {
        ExceptionUtils.checkNull("operation", operation);
        return ConcreteSyntaxUtils.getVariableDeclarations(((AOperation) operation).getParameters());
    }

    /**
     * Retrieves all the variable declarations for the passed in <code>standardDeclarator</code>.
     *
     * @param standardDeclarator the standard declartor for which to retrieve the VariableDeclaration instances.
     * @return VariableDeclaration[]
     */
    public static VariableDeclaration[] getVariableDeclarations(AStandardDeclarator standardDeclarator)
    {
        ExceptionUtils.checkNull("standardDeclarator", standardDeclarator);
        return ConcreteSyntaxUtils.getVariableDeclarations(standardDeclarator.getVariableDeclarationList());
    }

    /**
     * Creates a new VariableDeclaration from the passed in PVariableDeclaration.
     *
     * @param variableDeclaration the PVariableDeclaration that the new VariableDeclaration will be created from.
     * @param initialValue        the initial value of the variable declaration.
     * @return VariableDeclaration the new VariableDeclaration
     */
    protected static VariableDeclaration newVariableDeclaration(PVariableDeclaration variableDeclaration,
                                                                PEqualExpression initialValue)
    {
        ExceptionUtils.checkNull("variableDeclaration", variableDeclaration);

        AVariableDeclaration declaration = (AVariableDeclaration) variableDeclaration;
        ATypeDeclaration typeDeclaration = (ATypeDeclaration) declaration.getTypeDeclaration();
        String type = null;
        String name = ObjectUtils.toString(declaration.getName()).trim();
        if (typeDeclaration != null)
        {
            type = ObjectUtils.toString(typeDeclaration.getType());
        }
        return new VariableDeclarationImpl(name, type, ObjectUtils.toString(initialValue).trim());
    }

    /**
     * Creates an array of VariableDeclaration[] from the passed in PVariableDeclarationList.
     *
     * @param variableDeclarationList the PVariableDeclarationList that the new VariableDeclaration will be created
     *                                from.
     * @return VariableDeclaration[] the new VariableDeclaration array
     */
    public static VariableDeclaration[] getVariableDeclarations(PVariableDeclarationList variableDeclarationList)
    {

        Collection declarations = new ArrayList();

        if (variableDeclarationList != null)
        {
            AVariableDeclarationList variables = (AVariableDeclarationList) variableDeclarationList;

            // add the first one
            declarations.add(ConcreteSyntaxUtils.newVariableDeclaration(variables.getVariableDeclaration(),
                    variables.getVariableDeclarationValue()));

            // add the rest
            List<AVariableDeclarationListTail> variableTails = variables.getVariableDeclarationListTail();
            if (variableTails != null)
            {
                for (AVariableDeclarationListTail tail : variableTails)
                {
                    declarations.add(
                            ConcreteSyntaxUtils.newVariableDeclaration(tail.getVariableDeclaration(),
                                    tail.getVariableDeclarationValue()));
                }
            }
        }
        return (VariableDeclaration[]) declarations.toArray(new VariableDeclaration[declarations.size()]);
    }

    /**
     * Gets all the parameters from the <code>featureCall</code> instance.
     *
     * @param featureCall the featureCall for which to retrieve the parameters
     * @return List the list containing any parameters retrieved, or an empty array if none could be retrieved
     */
    public static List getParameters(AFeatureCall featureCall)
    {
        List parameters = new ArrayList();
        if (featureCall != null)
        {
            parameters = getParameters(featureCall.getFeatureCallParameters());
        }
        return parameters;
    }

    /**
     * Gets all the parameters from the <code>featureCall</code> instance as a comma separated String.
     *
     * @param featureCall the featureCall from which to retrieve the parameters
     * @return String the comma separated String
     */
    public static String getParametersAsString(AFeatureCall featureCall)
    {
        return getParametersAsString(featureCall.getFeatureCallParameters());
    }

    /**
     * Gets all the parameters from the <code>PFeatureCallParameters</code> instance as a comma separated String.
     *
     * @param featureCallParameters the featureCallParameters from which to retrieve the parameters
     * @return String the comma separated String
     */
    public static String getParametersAsString(PFeatureCallParameters featureCallParameters)
    {
        return StringUtils.join(ConcreteSyntaxUtils.getParameters(featureCallParameters).iterator(), ",");
    }

    /**
     * Gets all the parameters from the <code>callParameters</code> instance.
     *
     * @param callParameters the callParameters for which to retrieve the parameters
     * @return List the list containing any parameters retrieved, or an empty array if none could be retrieved
     */
    private static List getParameters(PFeatureCallParameters callParameters)
    {
        List parameters = new ArrayList();
        if (callParameters != null)
        {
            PActualParameterList parameterList = ((AFeatureCallParameters) callParameters).getActualParameterList();
            if (parameterList != null)
            {
                AActualParameterList params = (AActualParameterList) parameterList;

                // add the first param if it exists
                String firstParam = TranslationUtils.trimToEmpty(params.getExpression());
                if (StringUtils.isNotBlank(firstParam))
                {
                    parameters.add(firstParam);
                }

                // now add the rest of the params which are contained in the
                // tail
                List<ACommaExpression> restOfParams = params.getCommaExpression();
                if (restOfParams != null && !restOfParams.isEmpty())
                {
                    for (ACommaExpression parameterListTail : restOfParams)
                    {
                        parameters.add(TranslationUtils.trimToEmpty(parameterListTail.getExpression()));
                    }
                }
            }
        }
        return parameters;
    }

    /**
     * Gets the left and right expressions of a PRelationalExpression and puts then into a List. The left expression
     * will be the first expression in the list.
     *
     * @param relationalExpression
     * @return the left and right parenthesis in [0] and [1] of the String[]
     */
    public static String[] getLeftAndRightExpressions(PRelationalExpression relationalExpression)
    {
        String[] expressions = new String[2];
        ARelationalExpression expression = (ARelationalExpression) relationalExpression;

        // set the left expression
        expressions[0] = TranslationUtils.trimToEmpty(expression.getAdditiveExpression());

        ARelationalExpressionTail expressionTail = (ARelationalExpressionTail) expression.getRelationalExpressionTail();

        // set the right expression
        expressions[1] = TranslationUtils.trimToEmpty(expressionTail.getAdditiveExpression());

        return expressions;
    }

    /**
     * Concatenates the type from the passed in name and pathNameTail.
     *
     * @param name         the starting name of the type
     * @param pathNameTail the tail pieces of the name
     * @return String the concatenated name.
     */
    public static String getType(TName name, List pathNameTail)
    {
        StringBuffer type = ConcreteSyntaxUtils.concatContents(pathNameTail);
        type.insert(0, TranslationUtils.trimToEmpty(name));
        return StringUtils.deleteWhitespace(type.toString());
    }

    /**
     * Gets the "real" primary expression, as opposed to the primary expression retrieved from the parser syntax (since
     * it leaves off any navigational relationships).
     *
     * @param expression the APosfixExpression instance for which to retrieve the primary expression
     * @return String the "real" primary expression or the passed in expression.
     */
    public static String getPrimaryExpression(APropertyCallExpression expression)
    {
        StringBuilder primaryExpression = new StringBuilder();
        if (expression != null)
        {
            // append the first part of the primary expression
            primaryExpression.append(TranslationUtils.trimToEmpty(expression.getPrimaryExpression()));
            List expressionTail = expression.getPropertyCallExpressionTail();
            if (!expressionTail.isEmpty())
            {
                for (Object object : expressionTail)
                {
                    final String tail = TranslationUtils.trimToEmpty(object);
                    // beak out if we encounter an arrow feature call
                    if (tail.contains(ARROW_FEATURE_CALL))
                    {
                        break;
                    }
                    // only append to the expression if not an operation
                    if (tail.indexOf('(') == -1)
                    {
                        primaryExpression.append(tail);
                    }
                }
            }
        }
        return StringUtils.deleteWhitespace(primaryExpression.toString());
    }

    /**
     * Gets all feature calls from the passed in APropertyCallExpression instance.
     *
     * @param expression the APosfixExpression instance for which to retrieve the primary expression
     * @return String the "real" primary expression of the passed in expression.
     */
    public static List getFeatureCalls(APropertyCallExpression expression)
    {
        final String methodName = "ConcreteSyntaxUtils.getFeatureCalls";
        if (logger.isDebugEnabled())
        {
            logger.debug("performing " + methodName + " with expression --> '" + expression + '\'');
        }
        List featureCalls = new ArrayList();
        if (expression != null)
        {
            List tails = expression.getPropertyCallExpressionTail();
            if (tails != null && !tails.isEmpty())
            {
                for (int ctr = 0; ctr < tails.size(); ctr++)
                {
                    featureCalls.add(TranslationUtils.getProperty(tails.get(ctr), "featureCall"));
                }
            }
        }
        return featureCalls;
    }

    /**
     * Indicates an arrow feature call.
     */
    private static final String ARROW_FEATURE_CALL = "->";

    /**
     * Gets the navigational path from the given <code>expression</code> that occurs after an arrow feature call. If the
     * the expression contains an arrow feature call, then the navigational expression is any expression navigating on
     * the result of an arrow feature call (otherwise it's an empty string).
     *
     * @param expression the expression from which to retrieve the navigational path.
     * @return the navigational path.
     */
    public static String getArrowFeatureCallResultNavigationalPath(APropertyCallExpression expression)
    {
        StringBuilder path = new StringBuilder();
        if (OCLPatterns.isCollectionOperationResultNavigationalPath(expression))
        {
            List featureCalls = getFeatureCalls(expression);
            int size = featureCalls.size();
            if (size > 1)
            {
                for (int ctr = 1; ctr < size; ctr++)
                {
                    String featureCall = TranslationUtils.trimToEmpty(featureCalls.get(ctr));
                    if (featureCall.contains(ARROW_FEATURE_CALL) || featureCall.indexOf('(') != -1)
                    {
                        break;
                    }
                    path.append(featureCall);
                    if (ctr != size - 1)
                    {
                        path.append('.');
                    }
                }
            }
        }
        return path.toString();
    }

    /**
     * Loads a List of variable declaration names from the <code>variableDeclarations</code>
     *
     * @param variableDeclarations an array of VariableDeclaration objects
     * @return List the list of argument names as Strings/
     */
    public static List getArgumentNames(VariableDeclaration[] variableDeclarations)
    {
        List names = new ArrayList();
        for (int ctr = 0; ctr < variableDeclarations.length; ctr++)
        {
            names.add(variableDeclarations[ctr].getName());
        }
        return names;
    }
}
