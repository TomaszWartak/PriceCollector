package com.dev4lazy.pricecollector.utils;

public abstract class TaskLink {

    private TaskChain taskChain;
    private TaskLink nextTaskLink;

    public TaskLink(TaskChain taskChain) {
        this.taskChain = taskChain;
    }

    private TaskLink getNextTaskLink() {
        return nextTaskLink;
    }

    protected void setNextTaskLink(TaskLink nextTaskLink) {
        this.nextTaskLink = nextTaskLink;
    }

    /**
     * Uruchamia następny TaskLink (jeśli jest) z danymi potrzebnymi do wykonania.
     * Musi byc unmieszczona jako ostatnia instrukcja w kodzie każdego TaskLink.
     *
     * @param data (Object...) - dane do przekazania do nastęnego TaskLink.
     */
    public void runNextTaskLink(Object... data) {
        if (getNextTaskLink() != null) {
            getNextTaskLink().takeData(data);
            getNextTaskLink().doIt();
        } else {
            taskChain.getChain().clear();
        }
    }

    /**
     * Przekazuje dane potrzebne do wykonania następnego TaskLink.
     *
     * @param data (Object...) - dane do przekazania do nastęnego TaskLink.
     */
    abstract protected void takeData(Object... data);

    /**
     * Wywołuje kod do wykonania.
     */
    abstract protected void doIt();

}
