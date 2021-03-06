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
package org.xwiki.contrib.rendering.markdown.markdown12.internal.parser;

import java.util.Collections;
import java.util.Deque;
import java.util.Map;

import org.xwiki.rendering.listener.Listener;

import com.vladsch.flexmark.ast.Code;
import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.ast.IndentedCodeBlock;
import com.vladsch.flexmark.ast.NodeVisitor;
import com.vladsch.flexmark.ast.VisitHandler;
import com.vladsch.flexmark.ast.Visitor;

/**
 * Handle Code events.
 *
 * @version $Id$
 * @since 8.4
 */
public class CodeNodeVisitor extends AbstractNodeVisitor
{
    static <V extends CodeNodeVisitor> VisitHandler<?>[] VISIT_HANDLERS(final V visitor)
    {
        return new VisitHandler<?>[]{
                new VisitHandler<>(Code.class, new Visitor<Code>()
                {
                    @Override
                    public void visit(Code node)
                    {
                        visitor.visit(node);
                    }
                }),
                new VisitHandler<>(FencedCodeBlock.class, new Visitor<FencedCodeBlock>()
                {
                    @Override
                    public void visit(FencedCodeBlock node)
                    {
                        visitor.visit(node);
                    }
                }),
                new VisitHandler<>(IndentedCodeBlock.class, new Visitor<IndentedCodeBlock>()
                {
                    @Override
                    public void visit(IndentedCodeBlock node)
                    {
                        visitor.visit(node);
                    }
                })
        };
    }

    /**
     * Id of the code macro.
     */
    private static final String CODE_MACRO_ID = "code";

    public CodeNodeVisitor(NodeVisitor visitor, Deque<Listener> listeners)
    {
        super(visitor, listeners);
    }

    public void visit(Code node)
    {
        // Since XWiki doesn't have a Code Block we generate a Code Macro Block
        getListener().onMacro(CODE_MACRO_ID, Collections.EMPTY_MAP, node.getText().toString(), true);
    }

    public void visit(FencedCodeBlock node)
    {
        // Since XWiki doesn't have a Code Block we generate a Code Macro Block
        Map<String, String> parameters;
        if (node.getInfo() != null) {
            parameters = Collections.singletonMap("language", node.getInfo().toString());
        } else {
            parameters = Collections.EMPTY_MAP;
        }

        // Flexmark puts trailing newline in the HTML block so we need to remove it.
        String content = node.getContentChars().toString().trim();
        getListener().onMacro(CODE_MACRO_ID, parameters, content, false);
    }

    public void visit(IndentedCodeBlock node)
    {
        // Since XWiki doesn't have a Code Block we generate a Code Macro Block
        getListener().onMacro(CODE_MACRO_ID, Collections.EMPTY_MAP, node.getContentChars().toString(), false);
    }
}
