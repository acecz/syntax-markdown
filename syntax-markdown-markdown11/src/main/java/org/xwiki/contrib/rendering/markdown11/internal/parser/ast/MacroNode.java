/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.rendering.markdown11.internal.parser.ast;

import java.util.LinkedList;
import java.util.List;

import org.parboiled.common.ImmutableList;
import org.pegdown.ast.Node;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.Visitor;

/**
 * Represents a macro node.
 *
 * @version $Id $
 * @since 5.2RC1
 */
public class MacroNode extends SuperNode
{
    private final String macroId;

    private final boolean isInline;

    private List<MacroParameterNode> parameters = new LinkedList<MacroParameterNode>();

    /**
     * @param macroId id (name) of the XWiki macro
     * @param isInline if the macro is located in a inline content (like paragraph, etc.)
     */
    public MacroNode(String macroId, boolean isInline)
    {
        this.macroId = macroId;
        this.isInline = isInline;
    }

    /**
     * @return id (name) of the XWiki macro
     */
    public String getMacroId()
    {
        return this.macroId;
    }

    /**
     * @return if the macro is located in a inline content (like paragraph, etc.)
     */
    public boolean isInline()
    {
        return this.isInline;
    }

    /**
     * @return immutable list of the macro parameters
     */
    public ImmutableList<MacroParameterNode> getParameters()
    {
        return ImmutableList.copyOf(this.parameters);
    }

    /**
     * @param node parameter node to append
     * @return {@literal true}
     */
    public boolean addParameter(MacroParameterNode node)
    {
        this.parameters.add(node);
        return true;
    }

    @Override
    public void accept(Visitor visitor)
    {
        visitor.visit((Node) this);
    }
}
