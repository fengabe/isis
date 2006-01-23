package org.nakedobjects.viewer.skylark.metal;

import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Color;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.Style;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAreaType;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.basic.ActionDialogSpecification;
import org.nakedobjects.viewer.skylark.core.AbstractView;
import org.nakedobjects.viewer.skylark.special.ScrollBorder;

import java.util.StringTokenizer;


public class DetailedMessageViewSpecification implements ViewSpecification {

    public boolean canDisplay(Content content) {
        return content instanceof MessageContent && ((MessageContent) content).getDetail() != null;
    }

    public String getName() {
        return "Detailed Message";
    }

    public View createView(Content content, ViewAxis axis) {
        ButtonAction actions[] = new ButtonAction[] {
                new ActionDialogSpecification.CancelAction()
         };
        return new DialogBorder(new ButtonBorder(actions,new ScrollBorder(new DetailedMessageView(content, this, null))), true);
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReplaceable() {
        return false;
    }

    public boolean isSubView() {
        return false;
    }
}

class DetailedMessageView extends AbstractView {
    protected DetailedMessageView(Content content, ViewSpecification specification, ViewAxis axis) {
        super(content, specification, axis);
    }

    public Size getRequiredSize() {
        Size size = new Size();
        size.extendHeight(Style.TITLE.getTextHeight());
        size.extendHeight(30);

        String message = ((MessageContent) getContent()).getMessage();
        size.ensureWidth(Style.NORMAL.stringWidth(message));
        size.extendHeight(Style.NORMAL.getTextHeight());
        size.extendHeight(30);
       
        String detail = ((MessageContent) getContent()).getDetail();
        StringTokenizer st = new StringTokenizer(detail, "\n\r");
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            size.ensureWidth((line.startsWith("\t") ? 20 : 0) + Style.NORMAL.stringWidth(line));
            size.extendHeight(Style.NORMAL.getTextHeight());
        }

        size.extend(40, 20);
        return size;
    }
    
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        int left = 0;
        int top = 0;
        int width = getSize().getWidth();
        int height = getSize().getHeight();
        
        canvas.drawSolidRectangle(left, top, width - 1, height -1, Style.WHITE);
        canvas.drawRectangle(left, top, width - 1, height - 1, Style.BLACK);

 
        left = 20;
        top += Style.TITLE.getTextHeight();
        
        String message = ((MessageContent) getContent()).getMessage();
        String heading = ((MessageContent) getContent()).title();
        String detail = ((MessageContent) getContent()).getDetail();

        canvas.drawText(heading, left, top, Color.RED, Style.TITLE);
        top += 30;
        canvas.drawText(message ,left, top, Color.RED, Style.NORMAL);

        top += 30;
        StringTokenizer st = new StringTokenizer(detail, "\n\r");
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            canvas.drawText(line,left + (line.startsWith("\t") ? 20 : 00), top, Color.BLACK, Style.NORMAL);
            top += Style.NORMAL.getTextHeight();
        }
        

    }
    
    public ViewAreaType viewAreaType(Location mouseLocation) {
        return ViewAreaType.VIEW;
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2005 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */