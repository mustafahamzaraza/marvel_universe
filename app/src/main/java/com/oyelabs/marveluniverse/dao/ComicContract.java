package com.oyelabs.marveluniverse.dao;

import android.provider.BaseColumns;



public interface ComicContract extends BaseColumns {
    String TB_COMIC    = "Comic";

    String COL_ID_WEB      = "idweb";
    String COL_TITLE       = "title";
    String COL_DESCRIPTION = "description";
    String COL_PAGERCOUNT  = "pager_count";
    String COL_MODIFIED    = "modified";
    String COL_ISBN        = "isbn";
    String COL_EAN         = "ean";



}

