/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openwes.workflow;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public interface CommandWatcher {

    public void onComplete();

    public void onFail();

    public void onError(Throwable t);

}
