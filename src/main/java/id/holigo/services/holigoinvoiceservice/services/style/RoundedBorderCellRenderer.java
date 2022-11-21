package id.holigo.services.holigoinvoiceservice.services.style;

import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;

public class RoundedBorderCellRenderer extends CellRenderer {

    protected boolean isColoredBackground;
    public RoundedBorderCellRenderer(Cell modelElement, boolean isColoredBackground) {
        super(modelElement);
        modelElement.setBorder(Border.NO_BORDER);
        this.isColoredBackground = isColoredBackground;
    }

    @Override
    public void drawBackground(DrawContext drawContext) {
        Rectangle rect = getOccupiedAreaBBox();
        PdfCanvas canvas = drawContext.getCanvas();
        canvas
                .saveState()
                .roundRectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight() , 6)
                .setFillColor(new DeviceRgb(209, 244, 206))
                .setLineWidth(0);
        if (isColoredBackground) {
            canvas.resetStrokeColorRgb();
            canvas.setStrokeColor(new DeviceRgb(209, 244, 206));
            canvas.setFillColor(new DeviceRgb(209, 244, 206)).fillStroke();
        } else {
            canvas.stroke();
        }
        canvas.restoreState();

    }
}
