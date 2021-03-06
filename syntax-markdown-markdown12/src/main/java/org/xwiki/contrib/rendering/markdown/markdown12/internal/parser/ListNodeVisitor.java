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

import org.xwiki.rendering.listener.ListType;
import org.xwiki.rendering.listener.Listener;
import org.xwiki.rendering.listener.WrappingListener;

import com.vladsch.flexmark.ast.BulletList;
import com.vladsch.flexmark.ast.BulletListItem;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ast.NodeVisitor;
import com.vladsch.flexmark.ast.OrderedList;
import com.vladsch.flexmark.ast.OrderedListItem;
import com.vladsch.flexmark.ast.VisitHandler;
import com.vladsch.flexmark.ast.Visitor;
import com.vladsch.flexmark.ext.definition.DefinitionItem;
import com.vladsch.flexmark.ext.definition.DefinitionList;
import com.vladsch.flexmark.ext.definition.DefinitionTerm;

/**
 * Handle list events.
 *
 * @version $Id$
 * @since 8.4
 */
public class ListNodeVisitor extends AbstractNodeVisitor
{
    static <V extends ListNodeVisitor> VisitHandler<?>[] VISIT_HANDLERS(final V visitor)
    {
        return new VisitHandler<?>[]{
                new VisitHandler<>(BulletList.class, new Visitor<BulletList>()
                {
                    @Override
                    public void visit(BulletList node)
                    {
                        visitor.visit(node);
                    }
                }),
                new VisitHandler<>(BulletListItem.class, new Visitor<BulletListItem>()
                {
                    @Override
                    public void visit(BulletListItem node)
                    {
                        visitor.visit(node);
                    }
                }),
                new VisitHandler<>(OrderedList.class, new Visitor<OrderedList>()
                {
                    @Override
                    public void visit(OrderedList node)
                    {
                        visitor.visit(node);
                    }
                }),
                new VisitHandler<>(OrderedListItem.class, new Visitor<OrderedListItem>()
                {
                    @Override
                    public void visit(OrderedListItem node)
                    {
                        visitor.visit(node);
                    }
                }),
                new VisitHandler<>(DefinitionList.class, new Visitor<DefinitionList>()
                {
                    @Override
                    public void visit(DefinitionList node)
                    {
                        visitor.visit(node);
                    }
                }),
                new VisitHandler<>(DefinitionTerm.class, new Visitor<DefinitionTerm>()
                {
                    @Override
                    public void visit(DefinitionTerm node)
                    {
                        visitor.visit(node);
                    }
                }),
                new VisitHandler<>(DefinitionItem.class, new Visitor<DefinitionItem>()
                {
                    @Override
                    public void visit(DefinitionItem node)
                    {
                        visitor.visit(node);
                    }
                })
        };
    }

    /**
     * Swallow paragraphs (for example we don't want to generate paragraphs for list items since the XWiki model
     * doesn't wrap list item content inside paragraphs).
     */
    private class ParagraphWrappingListener extends WrappingListener
    {
        @Override
        public void beginParagraph(Map<String, String> parameters)
        {
            // Ignore
        }

        @Override
        public void endParagraph(Map<String, String> parameters)
        {
            // Ignore
        }
    }

    public ListNodeVisitor(NodeVisitor visitor, Deque<Listener> listeners)
    {
        super(visitor, listeners);
    }

    public void visit(BulletList node)
    {
        getListener().beginList(ListType.BULLETED, Collections.EMPTY_MAP);
        getVisitor().visitChildren(node);
        getListener().endList(ListType.BULLETED, Collections.EMPTY_MAP);
    }

    public void visit(BulletListItem node)
    {
        visitListItem(node);
    }

    public void visit(OrderedList node)
    {
        getListener().beginList(ListType.NUMBERED, Collections.EMPTY_MAP);
        getVisitor().visitChildren(node);
        getListener().endList(ListType.NUMBERED, Collections.EMPTY_MAP);
    }

    public void visit(OrderedListItem node)
    {
        visitListItem(node);
    }

    public void visit(DefinitionList node)
    {
        getListener().beginDefinitionList(Collections.EMPTY_MAP);
        getVisitor().visitChildren(node);
        getListener().endDefinitionList(Collections.EMPTY_MAP);
    }

    public void visit(DefinitionTerm node)
    {
        getListener().beginDefinitionTerm();
        WrappingListener paragraphListener = new ParagraphWrappingListener();
        paragraphListener.setWrappedListener(getListener());
        pushListener(paragraphListener);
        getVisitor().visitChildren(node);
        popListener();
        getListener().endDefinitionTerm();
    }

    public void visit(DefinitionItem node)
    {
        getListener().beginDefinitionDescription();
        visitChildrenAndSwallowParagraphs(node);
        getListener().endDefinitionDescription();
    }

    private void visitListItem(Node node)
    {
        getListener().beginListItem();
        visitChildrenAndSwallowParagraphs(node);
        getListener().endListItem();
    }

    private void visitChildrenAndSwallowParagraphs(Node node)
    {
        WrappingListener paragraphListener = new ParagraphWrappingListener();
        paragraphListener.setWrappedListener(getListener());
        pushListener(paragraphListener);
        getVisitor().visitChildren(node);
        popListener();
    }
}
