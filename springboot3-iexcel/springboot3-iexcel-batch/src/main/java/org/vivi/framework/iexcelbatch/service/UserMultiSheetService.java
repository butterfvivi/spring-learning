package org.vivi.framework.iexcelbatch.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vivi.framework.iexcelbatch.entity.query.UserQuery;

public interface UserMultiSheetService {

    void exportExcel(HttpServletRequest request, HttpServletResponse response, UserQuery query);
}
