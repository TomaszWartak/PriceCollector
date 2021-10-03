package com.dev4lazy.pricecollector.utils;

import java.util.ArrayList;

public class TaskChain {

    private ArrayList<TaskLink> chain;

    public TaskChain() {
        chain = new ArrayList<>();
    }

    public void addTaskLink(TaskLink taskLink) {
        if (!chain.isEmpty()) {
            chain.get(chain.size() - 1).setNextTaskLink(taskLink);
        }
        chain.add(taskLink);
    }

    public void startIt() {
        if (!chain.isEmpty()) {
            chain.get(0).doIt();
        }
    }

    public ArrayList<TaskLink> getChain() {
        return chain;
    }
}
