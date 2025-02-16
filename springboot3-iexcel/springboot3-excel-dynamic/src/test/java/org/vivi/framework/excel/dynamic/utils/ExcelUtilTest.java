package org.vivi.framework.excel.dynamic.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vivi.framework.excel.dynamic1.handler.entity.ExcelHeader;
import org.vivi.framework.excel.dynamic1.utils.ExcelUtil;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class ExcelUtilTest {

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ExcelUtil excelUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDynamicExportExcel() throws Exception {
        List<ExcelHeader> headers = new ArrayList<>();
        List<Object> dataList = new ArrayList<>();
        String fileName = "testFile";
        String titleName = "Test Title";

        ExcelUtil.dynamicExportExcel(headers, dataList, response, fileName, titleName);

        verify(response, times(1)).getOutputStream();
    }
}