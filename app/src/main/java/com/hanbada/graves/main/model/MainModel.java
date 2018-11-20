package com.hanbada.graves.main.model;

import com.hanbada.graves.main.Main;

public class MainModel implements Main.ProvidedModelOps{

    private Main.RequiredPresenterOps mPresenter;
//    private DAO mDao;

    public MainModel(Main.RequiredPresenterOps presenter){
        this.mPresenter = presenter;

    }


}
