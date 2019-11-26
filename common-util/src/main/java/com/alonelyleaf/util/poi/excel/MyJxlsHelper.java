package com.alonelyleaf.util.poi.excel;

import org.jxls.area.Area;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.formula.FastFormulaProcessor;
import org.jxls.formula.FormulaProcessor;
import org.jxls.formula.StandardFormulaProcessor;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

import java.io.IOException;
import java.util.List;

/**
 * @author bijl
 * @date 2019/4/2
 */
public class MyJxlsHelper extends JxlsHelper {

    private String templateSheetName = "Template";

    private MyJxlsHelper(){
        super();
    }

    public static MyJxlsHelper getInstance() {
        return new MyJxlsHelper();
    }

    @Override
    public void processTemplate(Context context, Transformer transformer) throws IOException {

        super.getAreaBuilder().setTransformer(transformer);
        List<Area> xlsAreaList = super.getAreaBuilder().build();
        for (Area xlsArea : xlsAreaList) {
            xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
        }
        if (isProcessFormulas()) {
            for (Area xlsArea : xlsAreaList) {
                this.setFormulaProcessor(xlsArea);
                xlsArea.processFormulas();
            }
        }

        if (super.isDeleteTemplateSheet()) {
            transformer.deleteSheet(templateSheetName);
        }
        transformer.write();
    }

    private Area setFormulaProcessor(Area xlsArea) {
        FormulaProcessor fp = super.getFormulaProcessor();
        if (fp == null) {
            if (super.isUseFastFormulaProcessor()) {
                fp = new FastFormulaProcessor();
            } else {
                fp = new StandardFormulaProcessor();
            }
        }
        xlsArea.setFormulaProcessor(fp);
        return xlsArea;
    }

    public MyJxlsHelper setTemplateSheetName(String templateSheetName) {

        this.templateSheetName = templateSheetName;

        return this;
    }
}
