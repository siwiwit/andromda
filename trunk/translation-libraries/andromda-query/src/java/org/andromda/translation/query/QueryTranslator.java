package org.andromda.translation.query;

import java.util.List;

import org.andromda.core.translation.BaseTranslator;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.core.translation.node.AFeatureCall;
import org.andromda.core.translation.node.ALogicalExpressionTail;
import org.andromda.core.translation.node.AOperationContextDeclaration;
import org.andromda.core.translation.node.AParenthesesPrimaryExpression;
import org.andromda.core.translation.node.APropertyCallExpression;
import org.andromda.core.translation.node.ARelationalExpressionTail;
import org.andromda.core.translation.node.AStandardDeclarator;
import org.andromda.core.translation.node.PRelationalExpression;
import org.andromda.core.translation.node.TLParen;
import org.andromda.core.translation.node.TRParen;
import org.andromda.core.translation.syntax.VariableDeclaration;
import org.andromda.core.translation.syntax.impl.ConcreteSyntaxUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Performs translation to the following:
 * <ul>
 * <li>Hibernate-QL</li>
 * </ul>
 * 
 * @author Chad Brandon
 */
public class QueryTranslator
    extends BaseTranslator
{

    /**
     * Used to save the argument names for the operation which represents the
     * context of the expression being translated.
     */
    private List argumentNames = null;

    /**
     * Contains the select clause of the query.
     */
    private StringBuffer selectClause;

    /**
     * Called by super class to reset any objects.
     */
    protected void preProcess()
    {
        super.preProcess();
        this.selectClause = new StringBuffer();
        this.sortedByClause = new StringBuffer();
        this.declaratorCtr = 0;
    }

    /**
     * Stores the name of the initial declarator.
     */
    private short declaratorCtr;

    /**
     * True/false whether or not its the initial declarator. We care about this
     * because we must know what the first one is for differentiating between
     * the first declarator (the beginning of the query) and any other
     * declarators (the connecting associations).
     */
    protected boolean isInitialDeclarator()
    {
        boolean isInitialDeclarator = (this.declaratorCtr <= 0);
        this.declaratorCtr++;
        return isInitialDeclarator;
    }

    /**
     * Indicates whether or not the <code>name</code> is one of the arguments.
     * 
     * @return true if its an argument
     */
    protected boolean isArgument(String name)
    {
        boolean isArgument = this.argumentNames != null;
        if (isArgument)
        {
            isArgument = this.argumentNames.contains(name);
        }
        return isArgument;
    }

    /**
     * Gets the information about the operation which is the context of this
     * expression.
     */
    public void inAOperationContextDeclaration(
        AOperationContextDeclaration declaration)
    {
        super.inAOperationContextDeclaration(declaration);
        VariableDeclaration[] variableDeclarations = ConcreteSyntaxUtils
            .getVariableDeclarations(declaration.getOperation());
        // set the argument names in a List so that we can retieve them later
        this.argumentNames = ConcreteSyntaxUtils
            .getArgumentNames(variableDeclarations);
    }

    /**
     * Helps out with 'includesAll', replaces the {1} in the expression fragment
     * when a declarator is encountered (i.e. '| <variable name>')
     */
    public void inAStandardDeclarator(AStandardDeclarator declarator)
    {
        final String methodName = "QueryTranslator.inAStandardDeclarator";
        if (logger.isDebugEnabled())
            logger.debug("performing " + methodName + " with declarator --> "
                + declarator);

        String temp = this.selectClause.toString();

        String declaratorName = ConcreteSyntaxUtils
            .getVariableDeclarations(declarator)[0].getName();

        // by default we'll assume we're replacing the {1} arg.
        short replacement = 1;
        if (this.isInitialDeclarator())
        {
            // handle differently if its the initial declarator,
            // replacement is {0}
            replacement = 0;
        }

        // now replace {1} reference
        temp = TranslationUtils.replacePattern(temp, String
            .valueOf(replacement), declaratorName);
        this.selectClause = new StringBuffer(temp);
    }

    /**
     * Override to handle any propertyCall expressions ( i.e. exists(
     * <expression>), select( <expression>), etc.)
     * 
     * @see org.andromda.core.translation.analysis.DepthFirstAdapter#inAPropertyCallExpression(org.andromda.core.translation.node.APropertyCallExpression)
     */
    public void inAPropertyCallExpression(APropertyCallExpression expression)
    {
        this.handleTranslationFragment(expression);
    }

    /**
     * Override to handle any featureCall expressions ( i.e. sortedBy(
     * <expression>), etc.)
     * 
     * @see org.andromda.core.translation.analysis.DepthFirstAdapter#inAFeatureCallExpression(org.andromda.core.translation.node.APropertyCallExpression)
     */
    public void inAFeatureCall(AFeatureCall expression)
    {
        // don't handl all instances here, since it's handled
        // in the property call expression.
        if (!TranslationUtils.trimToEmpty(expression).matches(
            OCLFeatures.ALL_INSTANCES))
        {
            this.handleTranslationFragment(expression);
        }
    }

    /**
     * Override to deal with logical 'and, 'or', 'xor, ... expressions.
     * 
     * @see org.andromda.core.translation.analysis.DepthFirstAdapter#inALogicalExpressionTail(org.andromda.core.translation.node.ALogicalExpressionTail)
     */
    public void inALogicalExpressionTail(
        ALogicalExpressionTail logicalExpressionTail)
    {
        this.handleTranslationFragment(logicalExpressionTail);
    }

    /**
     * Override to deal with relational ' <, '>', '=', ... expressions.
     * 
     * @see org.andromda.core.translation.analysis.DepthFirstAdapter#inARelationalExpressionTail(org.andromda.core.translation.node.ARelationalExpressionTail)
     */
    public void inARelationalExpressionTail(
        ARelationalExpressionTail relationalExpressionTail)
    {
        this.handleTranslationFragment(relationalExpressionTail);
    }

    /**
     * A flag indicating whether or not we're in a parenthesis primary
     * expression. Allows us to keep track of whether or not the translated
     * expression requires left and right parenthesis.
     */
    private boolean parenthesis = false;

    /**
     * Override to deal with parenthesis expressions '( <expression>)'.
     * 
     * @see org.andromda.core.translation.analysis.DepthFirstAdapter#outAParenthesesPrimaryExpression(org.andromda.core.translation.node.AParenthesesPrimaryExpression)
     */
    public void inAParenthesesPrimaryExpression(
        AParenthesesPrimaryExpression expression)
    {
        parenthesis = true;
    }

    /**
     * @see org.andromda.core.translation.analysis.Analysis#caseTLParen(org.andromda.core.translation.node.TLParen)
     */
    public void caseTLParen(TLParen left)
    {
        if (this.parenthesis)
        {
            this.getExpression().appendSpaceToTranslatedExpression();
            this.getExpression().appendToTranslatedExpression(left);
        }
    }

    /**
     * @see org.andromda.core.translation.analysis.Analysis#caseTRParen(org.andromda.core.translation.node.TRParen)
     */
    public void caseTRParen(TRParen right)
    {
        if (this.parenthesis)
        {
            this.getExpression().appendSpaceToTranslatedExpression();
            this.getExpression().appendToTranslatedExpression(right);
            this.parenthesis = false;
        }
    }

    /**
     * Checks to see if the replacement is an argument and if so replaces the
     * {index} in the fragment with the 'argument' fragment from the template.
     * Otherwise replaces the {index} with the passed in replacement value.
     * 
     * @param fragment the fragment to perform replacement.
     * @param replacement the replacement string
     * @param index the index in the string to replace.
     * @return String the fragment with any replacements.
     */
    protected String replaceFragment(
        String fragment,
        String replacement,
        int index)
    {
        fragment = StringUtils.trimToEmpty(fragment);
        replacement = StringUtils.trimToEmpty(replacement);
        // if 'replacement' is an argument use that for the replacement
        // in the fragment
        if (this.isArgument(replacement))
        {
            final String argument = replacement;
            replacement = this.getTranslationFragment("argument");
            replacement = TranslationUtils.replacePattern(replacement, String
                .valueOf(0), argument);
        }
        fragment = TranslationUtils.replacePattern(fragment, String
            .valueOf(index), replacement);
        return fragment;
    }

    /**
     * Stores the name of the fragment that maps the tail of the select clause.
     */
    private static final String SELECT_CLAUSE_TAIL = "selectClauseTail";

    /**
     * Stores the name of the fragment that maps to the head of the sortedBy
     * clause.
     */
    private static final String SORTED_BY_CLAUSE_HEAD = "sortedByClauseHead";

    /**
     * Handles any final processing.
     */
    protected void postProcess()
    {
        super.postProcess();
        // create the final translated expression
        String selectClauseTail = this
            .getTranslationFragment(SELECT_CLAUSE_TAIL);
        String existingExpression = StringUtils.trimToEmpty(this
            .getExpression().getTranslatedExpression());

        if (StringUtils.isNotEmpty(selectClauseTail)
            && StringUtils.isNotEmpty(existingExpression))
        {
            this.selectClause.append(" ");
            this.selectClause.append(selectClauseTail);
            this.selectClause.append(" ");
        }

        this.getExpression().insertInTranslatedExpression(
            0,
            selectClause.toString());

        if (this.sortedByClause.length() > 0)
        {
            this.getExpression().appendSpaceToTranslatedExpression();
            this.getExpression().appendToTranslatedExpression(
                this.getTranslationFragment(SORTED_BY_CLAUSE_HEAD));
            this.getExpression().appendSpaceToTranslatedExpression();
            this.getExpression().appendToTranslatedExpression(
                this.sortedByClause);
        }

        // remove any extra space from parenthesis
        this.getExpression().replaceInTranslatedExpression("\\(\\s*", "(");
        this.getExpression().replaceInTranslatedExpression("\\s*\\)", ")");
    }

    /*------------------------- Handler methods ---------------------------------------*/

    /*------------------------- PropertyCallExpression Handler methods ---------------------*/

    public void handleSubSelect(String translation, Object node)
    {
        APropertyCallExpression propertyCallExpression = (APropertyCallExpression)node;

        String primaryExpression = ConcreteSyntaxUtils
            .getPrimaryExpression(propertyCallExpression);

        // set the association which the 'includesAll' indicates (which
        // is the first feature call from the list of feature calls)
        translation = this.replaceFragment(translation, TranslationUtils
            .trimToEmpty(primaryExpression), 0);

        this.selectClause.append(" ");
        this.selectClause.append(translation);
    }

    public void handleIsLike(String translation, Object node)
    {
        APropertyCallExpression propertyCallExpression = (APropertyCallExpression)node;
        List featureCalls = ConcreteSyntaxUtils
            .getFeatureCalls(propertyCallExpression);

        List params = params = ConcreteSyntaxUtils
            .getParameters((AFeatureCall)featureCalls.get(0));

        translation = this.replaceFragment(translation, TranslationUtils
            .deleteWhitespace(params.get(0)), 0);
        translation = this.replaceFragment(translation, TranslationUtils
            .deleteWhitespace(params.get(1)), 1);

        if (StringUtils.isNotEmpty(this.getExpression()
            .getTranslatedExpression()))
        {
            this.getExpression().appendSpaceToTranslatedExpression();
        }
        this.getExpression().appendToTranslatedExpression(translation);
    }

    public void handleSelect(String translation, Object node)
    {
        this.selectClause.append(translation);
    }

    public void handleExists(String translation, Object node)
    {
        APropertyCallExpression propertyCallExpression = (APropertyCallExpression)node;
        List featureCalls = ConcreteSyntaxUtils
            .getFeatureCalls(propertyCallExpression);

        // since the parameters can only be either dotFeatureCall or
        // arrowFeatureCall we try one or the other.
        String parameters = StringUtils.deleteWhitespace(ConcreteSyntaxUtils
            .getParametersAsString((AFeatureCall)featureCalls.get(0)));

        String primaryExpression = ConcreteSyntaxUtils
            .getPrimaryExpression(propertyCallExpression);

        translation = this.replaceFragment(translation, primaryExpression, 1);
        translation = this.replaceFragment(translation, parameters, 0);

        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }

    public void handleIsEmpty(String translation, Object node)
    {
        APropertyCallExpression propertyCallExpression = (APropertyCallExpression)node;

        String primaryExpression = ConcreteSyntaxUtils
            .getPrimaryExpression(propertyCallExpression);

        translation = this.replaceFragment(translation, primaryExpression, 0);

        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }

    private StringBuffer sortedByClause;

    public void handleSortedBy(String translation, Object node)
    {
        if (this.sortedByClause.length() > 0)
        {
            this.sortedByClause.append(", ");
        }
        this.sortedByClause.append(TranslationUtils
            .deleteWhitespace(ConcreteSyntaxUtils
                .getParametersAsString((AFeatureCall)node)));
    }

    /*------------------------- Logical Expression Handler (and, or, xor, etc.) ----------------------*/

    public void handleLogicalExpression(String translation, Object node)
    {
        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }

    /*------------------------- Relational Expression Handler (=, <, >, >=, etc.) --------------------*/

    public void handleRelationalExpression(String translation, Object node)
    {
        ARelationalExpressionTail relationalExpressionTail = (ARelationalExpressionTail)node;

        String[] leftAndRightExpressions = ConcreteSyntaxUtils
            .getLeftAndRightExpressions((PRelationalExpression)relationalExpressionTail
                .parent());

        String leftExpression = StringUtils
            .deleteWhitespace(leftAndRightExpressions[0]);
        String rightExpression = StringUtils
            .deleteWhitespace(leftAndRightExpressions[1]);

        translation = this.replaceFragment(translation, leftExpression, 0);
        translation = this.replaceFragment(translation, rightExpression, 1);

        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }
}
