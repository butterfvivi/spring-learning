package org.vivi.framework.ureport.simple.ureport.export;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.vivi.framework.ureport.simple.ureport.export.excel.high.ExcelProducer;
import org.vivi.framework.ureport.simple.ureport.export.pdf.PdfProducer;
import org.vivi.framework.ureport.simple.ureport.export.word.high.WordProducer;
import org.vivi.framework.ureport.simple.ureport.model.Report;

/**
 * 导出报表
 * @author Administrator
 *
 */
public class ExportUtils {
	
	@SuppressWarnings("serial")
	private static Map<ProducerEnum,Producer> producer = new HashMap<ProducerEnum, Producer>() {{
		put(ProducerEnum.EXCEL, new ExcelProducer());
		put(ProducerEnum.PDF, new PdfProducer());
		put(ProducerEnum.WORD, new WordProducer());
	}};
	
	private ExportUtils() {
		
	}

	public static void export(OutputStream out, Report report, ProducerEnum type) {
		Producer p = producer.get(type);
		if (p != null) {
			p.produce(report, out);
		}
	}
}
