package com.wsiz.wirtualny.presenter;

import java.util.ArrayList;

public interface FinancesContract {
    interface Presenter {
        ArrayList<String> getFinances();
    }

}