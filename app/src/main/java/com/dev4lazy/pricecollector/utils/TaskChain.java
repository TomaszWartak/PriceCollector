package com.dev4lazy.pricecollector.utils;

import java.util.ArrayList;

public class TaskChain {

    private ArrayList<TaskLink> chain;
    private TaskLink afterAllToDoTask = null;
    private TaskLink suspendAfterTask = null;
    private boolean suspended = false;


    public TaskChain() {
        chain = new ArrayList<>();
    }

    public ArrayList<TaskLink> getChain() {
        return chain;
    }

    public TaskLink getSuspendAfterTask() {
        return suspendAfterTask;
    }

    /**
     * Ustawia zadanie, po którym ma zostac wstrzymane przetwarzanie TaskChain.
     * Przetwarzanie może byc wznowione za pomocą wywołania metody resume().
     * @return this <- TaskChain
     */
    public TaskChain suspendHere() {
        suspendAfterTask = getLastTask();
        return this;
    }

    public void suspend( ) {
        suspended = true;
    }

    public void resume( Object... data ) {
        suspended = false;
        suspendAfterTask.getNextTaskLink().doIt( data );
        suspendAfterTask = null;
    }

    public boolean isSupended() {
        return suspended;
    }

    public boolean isNotSuspended() {
        return !suspended;
    }

    public TaskChain addTaskLink(TaskLink taskLink) {
        taskLink.setTaskChain( this );
        if (!chain.isEmpty()) {
            chain.get(chain.size() - 1).setNextTaskLink(taskLink);
        }
        chain.add(taskLink);
        return this;
    }

    public void startIt(Object... data) {
        if (!chain.isEmpty()) {
            // TODO XXX chain.get(0).takeData(data);
            chain.get(0).doIt(data);
        }
    }

    public void setAfterAllToDoTask(TaskLink taskLink) {
        afterAllToDoTask = taskLink;
    }

    public TaskLink getAfterAllToDoTask() {
        return afterAllToDoTask;
    }

    private TaskLink getLastTask() {
        if (chain.isEmpty()) {
            return null;
        } else {
            return chain.get(chain.size() - 1);
        }
    }

}
