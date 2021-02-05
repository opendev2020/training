package edu.cust.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import edu.cust.Env;
import edu.cust.util.page.Page;
import edu.cust.util.page.PageFactory;
import edu.cust.util.search.Search;

/**
 * Created by qh on 2017/4/18.
 */
public abstract class AbstractController {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected Env env;

    protected void list(int rows, int page, Search search, DAOTemplate<?> dao, Model model){
        Page p = PageFactory.getPage();
        p.setRecordNum(rows);
        p.setPageNum(page);
        List<?> result = p.getOnePage(search.buildSQL(dao), search.getParams(), dao);
        model.addAttribute("retCode", "OK");
        model.addAttribute("result", result);
        model.addAttribute("page", p);
    }
}
