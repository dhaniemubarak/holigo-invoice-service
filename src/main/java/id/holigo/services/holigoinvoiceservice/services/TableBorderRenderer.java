package id.holigo.services.holigoinvoiceservice.services;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TableRenderer;

public class TableBorderRenderer extends TableRenderer {
    public TableBorderRenderer(Table modelElement, Table.RowRange rowRange) {
        super(modelElement, rowRange);
    }

    public TableBorderRenderer(Table modelElement) {
        super(modelElement);
    }

    @Override
    protected void drawBorders(DrawContext drawContext) {
        Rectangle rect = getOccupiedAreaBBox();
        PdfPage currentPage = drawContext.getDocument().getPage(getOccupiedArea().getPageNumber());
        PdfCanvas aboveCanvas = new PdfCanvas(currentPage.newContentStreamAfter(), currentPage.getResources(), drawContext.getDocument());

        float lineWidth = 0.5f;
        rect.applyMargins(lineWidth / 2, lineWidth / 2, lineWidth / 2, lineWidth / 2, false);

        // drawing white rectangle over table border in order to hide it.
        aboveCanvas.saveState().setLineWidth(lineWidth).setStrokeColor(new DeviceRgb(255, 0, 0)).rectangle(rect).stroke().restoreState();

        // drawing red round rectangle which will be shown as boundary.
        aboveCanvas.saveState().setLineWidth(lineWidth).setStrokeColor(new DeviceRgb(209, 244, 206))
                .roundRectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight(), 5).stroke().restoreState();
        super.drawBorders(drawContext);
    }
}
