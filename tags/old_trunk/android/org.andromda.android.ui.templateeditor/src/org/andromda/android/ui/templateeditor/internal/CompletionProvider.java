package org.andromda.android.ui.templateeditor.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.android.core.cartridge.CartridgeParsingException;
import org.andromda.android.core.cartridge.CartridgeRegistry;
import org.andromda.android.core.cartridge.ICartridgeDescriptor;
import org.andromda.android.core.cartridge.ICartridgeJavaVariableDescriptor;
import org.andromda.android.core.cartridge.ICartridgeMetafacadeVariableDescriptor;
import org.andromda.android.core.cartridge.ICartridgeVariableContainer;
import org.andromda.android.core.cartridge.ICartridgeVariableDescriptor;
import org.andromda.android.core.internal.AndroidModelManager;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.core.util.ResourceResolver;
import org.andromda.android.ui.templateeditor.TemplateEditorPlugin;
import org.andromda.android.ui.util.SWTResourceManager;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.swt.graphics.Image;

import de.byteaction.velocity.editor.VelocityEditor;
import de.byteaction.velocity.editor.completion.ICompletionProvider;
import de.byteaction.velocity.vaulttec.ui.editor.text.VelocityTextGuesser;

/**
 * This completion provider calculates code completions for AndroMDA.
 *
 * @author Peter Friese
 * @since 25.01.2006
 */
public class CompletionProvider
        implements ICompletionProvider
{

    /** Icon for public methods. */
    private static final String ICON_METHOD_PUBLIC = "icons/methpub_obj.gif";

    /** Icon for private fields. */
    private static final String ICON_FIELD_PRIVATE = "icons/field_private_obj.gif";

    private static final String ICON_FIELD_PUBLIC = "icons/field_public_obj.gif";

    private IAndroidProject androidProject;

    /**
     * {@inheritDoc}
     */
    public Collection getExtraProposals(VelocityEditor editor, IFile file,
        IDocument doc,
        VelocityTextGuesser prefix,
        int offset)
    {
        Collection result = new ArrayList();

        IContainer cartridgeRoot = ResourceResolver.findCartridgeRoot(file);
        IPath templatePath = file.getProjectRelativePath();

        androidProject = AndroidModelManager.getInstance().getAndroidModel().getAndroidProject(file);

        ICartridgeDescriptor cartridgeDescriptor = CartridgeRegistry.getInstance()
                .getCartridgeDescriptor(cartridgeRoot);

        try
        {
            String text;
            int type = prefix.getType();
            switch (type)
            {
                case VelocityTextGuesser.TYPE_END:
                    text = "type_end: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TYPE_APOSTROPHE:
                    text = "type_apostrophe: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TYPE_INVALID:
                    text = "type_invalid: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TYPE_DIRECTIVE:
                    text = "type_directive: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TAG_DIRECTIVE:
                    text = "tag_directive: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TAG_CLOSE:
                    text = "tag_close: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TYPE_MEMBER_QUALIFIER:
                    result.addAll(getMemberProposals(editor, cartridgeDescriptor, prefix, offset, templatePath));
                    break;

                case VelocityTextGuesser.TYPE_VARIABLE:
                    result.addAll(getPropertyProposals(cartridgeDescriptor, prefix.getText(), offset, templatePath));
                    break;

            }
        }
        catch (CartridgeParsingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param cartridgeDescriptor
     * @param prefix
     * @param offset
     * @param templatePath
     * @return
     * @throws CartridgeParsingException
     */
    private Collection getMemberProposals(VelocityEditor editor, ICartridgeDescriptor cartridgeDescriptor,
        VelocityTextGuesser prefix,
        int offset,
        IPath templatePath) throws CartridgeParsingException
    {
        ArrayList result = new ArrayList();

        System.out.println("Prefix variable:" + prefix.getVariable());

        ICartridgeVariableContainer variableContainer = cartridgeDescriptor.getVariableDescriptors();
        Collection variableDescriptors = variableContainer.getAll();
        for (Iterator iter = variableDescriptors.iterator(); iter.hasNext();)
        {
            ICartridgeVariableDescriptor descriptor = (ICartridgeVariableDescriptor)iter.next();
            System.out.println("VariableDescriptor: " + descriptor.getName() + " [" + descriptor.hashCode() + "]");
            if (descriptor instanceof ICartridgeJavaVariableDescriptor)
            {
                if (descriptor instanceof ICartridgeMetafacadeVariableDescriptor)
                {
                    ICartridgeMetafacadeVariableDescriptor cartridgeMetafacadeVariableDescriptor = (ICartridgeMetafacadeVariableDescriptor)descriptor;
                    String variableTemplatePath = cartridgeMetafacadeVariableDescriptor.getTemplatePath();
                    // Make sure the template matches:
                    if (templatePath.toString().indexOf(variableTemplatePath) > 0)
                    {
                        System.out.println(variableTemplatePath + " is part of " + templatePath);
                        // Make sure the variable names match. TODO at this time, this is a very simple matching
                        // algorithm.
                        if (prefix.getVariable().startsWith(descriptor.getName()))
                        {
                            createMemberProposals(prefix, offset, result, cartridgeMetafacadeVariableDescriptor);
                        }
                    }
                    else
                    {
                        // System.out.println(variableTemplatePath + " is NOT part of " + templatePath);
                    }
                }
                else if (descriptor instanceof ICartridgeJavaVariableDescriptor)
                {
                    ICartridgeJavaVariableDescriptor cartridgeJavaVariableDescriptor = (ICartridgeJavaVariableDescriptor)descriptor;
                    String name = cartridgeJavaVariableDescriptor.getName();
                    if (prefix.getVariable().startsWith(name))
                    {
                        createMemberProposals(prefix, offset, result, cartridgeJavaVariableDescriptor);
                    }
                }

            }
        }
        return result;
    }

    /**
     * @param prefix
     * @param offset
     * @param result
     * @param cartridgeJavaVariableDescriptor
     * @throws CartridgeParsingException
     */
    private void createMemberProposals(VelocityTextGuesser prefix,
        int offset,
        ArrayList result,
        ICartridgeJavaVariableDescriptor cartridgeJavaVariableDescriptor) throws CartridgeParsingException
    {

        if (cartridgeJavaVariableDescriptor instanceof ICartridgeMetafacadeVariableDescriptor)
        {
            ICartridgeMetafacadeVariableDescriptor cartridgeMetafacadeVariableDescriptor = (ICartridgeMetafacadeVariableDescriptor)cartridgeJavaVariableDescriptor;
            if (cartridgeMetafacadeVariableDescriptor.isCollection())
            {
                IJavaProject javaProject = JavaCore.create(androidProject.getProject());
                try
                {
                    IType type = javaProject.findType("java.util.Collection");
                    createMemberProposals(prefix, offset, result, cartridgeJavaVariableDescriptor, type);
                }
                catch (JavaModelException e)
                {
                    throw new CartridgeParsingException(e);
                }
            }
            else
            {
                IType type = cartridgeJavaVariableDescriptor.getType();
                createMemberProposals(prefix, offset, result, cartridgeJavaVariableDescriptor, type);
            }
        }
    }

    /**
     * @param prefix
     * @param offset
     * @param result
     * @param cartridgeJavaVariableDescriptor
     * @param type
     * @throws CartridgeParsingException
     */
    private void createMemberProposals(VelocityTextGuesser prefix,
        int offset,
        ArrayList result,
        ICartridgeJavaVariableDescriptor cartridgeJavaVariableDescriptor,
        IType type) throws CartridgeParsingException
    {
        IMethod[] methods;
        try
        {
            methods = type.getMethods();
            for (int i = 0; i < methods.length; i++)
            {
                IMethod method = methods[i];
                String elementName = method.getElementName();
                String signature = getParameterSignature(method);
                String displayText = elementName + " (" + signature + ") - " + type.getElementName();
                if (elementName.startsWith(prefix.getText()))
                {
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, elementName, displayText,
                            ICON_METHOD_PUBLIC));
                }
            }
        }
        catch (JavaModelException e)
        {
            throw new CartridgeParsingException(e);
        }
    }

    /**
     * Creates a String representing the parameters of the given method.
     *
     * @param method The method to be described.
     * @return A String representing the parameter signature.
     *
     * @throws JavaModelException If a problem arises.
     */
    private String getParameterSignature(IMethod method) throws JavaModelException
    {
        String result = "";
        String[] parameterNames = method.getParameterNames();
        if (method.getNumberOfParameters() > 0)
        {
            for (int j = 0; j < parameterNames.length; j++)
            {
                result += parameterNames[j];
                if (j < parameterNames.length - 1)
                {
                    result += ", ";
                }

            }
        }
        return result;
    }

    /**
     * @param cartridgeDescriptor
     * @param templatePath
     * @param String
     * @return
     * @throws CartridgeParsingException
     */
    private Collection getPropertyProposals(ICartridgeDescriptor cartridgeDescriptor,
        String prefix,
        int offset,
        IPath templatePath) throws CartridgeParsingException
    {
        Map proposals = new HashMap();

        ICartridgeVariableContainer variableContainer = cartridgeDescriptor.getVariableDescriptors();
        Collection variableDescriptors = variableContainer.getAll();
        for (Iterator iter = variableDescriptors.iterator(); iter.hasNext();)
        {
            ICartridgeVariableDescriptor descriptor = (ICartridgeVariableDescriptor)iter.next();
            String proposalText = descriptor.getName();
            if (prefix.length() == 0 || proposalText.startsWith(prefix))
            {
                String icon;
                String displayString = "$" + proposalText;
                if (descriptor instanceof ICartridgeJavaVariableDescriptor)
                {
                    ICartridgeJavaVariableDescriptor javaVariableDescriptor = (ICartridgeJavaVariableDescriptor)descriptor;
                    if (javaVariableDescriptor.getType() != null)
                    {
                        displayString += " - " + javaVariableDescriptor.getType().getElementName();
                    }
                    icon = ICON_FIELD_PUBLIC;
                }
                else
                {
                    icon = ICON_FIELD_PRIVATE;
                }
                CompletionProposal proposal = createSimpleCompletionProposal(prefix, offset, proposalText,
                        displayString, icon);
                if (!proposals.containsKey(proposalText))
                {
                    proposals.put(proposalText, proposal);
                }
            }
        }

        return proposals.values();
    }

    /**
     * @param prefix
     * @param offset
     * @param text
     * @return
     */
    private CompletionProposal createSimpleCompletionProposal(String prefix,
        int offset,
        String text)
    {
        return new CompletionProposal(text, offset, prefix.length(), text.length(), null, text, null, null);
    }

    /**
     * @param prefix
     * @param offset
     * @param text
     * @return
     */
    private CompletionProposal createSimpleCompletionProposal(String prefix,
        int offset,
        String text,
        String icon)
    {
        Image image = SWTResourceManager.getPluginImage(TemplateEditorPlugin.getDefault(), icon);
        return new CompletionProposal(text, offset, prefix.length(), text.length(), image, text, null, null);
    }

    /**
     * @param prefix
     * @param offset
     * @param text
     * @return
     */
    private CompletionProposal createSimpleCompletionProposal(String prefix,
        int offset,
        String text,
        String displayString,
        String icon)
    {
        Image image = SWTResourceManager.getPluginImage(TemplateEditorPlugin.getDefault(), icon);
        return new CompletionProposal(text, offset, prefix.length(), text.length(), image, displayString, null, null);
    }

}