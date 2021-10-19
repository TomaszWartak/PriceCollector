package com.dev4lazy.pricecollector.utils;

public abstract class TaskLink {

    private TaskChain taskChain;
    private TaskLink nextTaskLink;

    protected void setTaskChain( TaskChain taskChain ) {
        this.taskChain = taskChain;
    }

    public TaskChain getTaskChain() {
        return taskChain;
    }

    protected void setNextTaskLink(TaskLink nextTaskLink) {
        this.nextTaskLink = nextTaskLink;
    }

    protected TaskLink getNextTaskLink() {
        return nextTaskLink;
    }

    /**
     * Uruchamia następny TaskLink (jeśli jest) z danymi potrzebnymi do wykonania.
     * Musi byc unmieszczona jako ostatnia instrukcja w kodzie każdego TaskLink.
     *
     * @param data (Object...) - dane do przekazania do nastęnego TaskLink.
     */
    protected void runNextTaskLink( Object... data ) {
        if (taskChain.isNotSuspended()) {
            if (isNextTaskLink()) {
                if (needToBeSuspendedAfterThis()) {
                    taskChain.suspend( );
                } else {
                    nextTaskLink.doIt( data );
                }
            } else {
                TaskLink afterAllToDo = taskChain.getAfterAllToDoTask();
                if (isAfterAllToDoTask(afterAllToDo)) {
                    afterAllToDo.doIt( data );
                    taskChain.setAfterAllToDoTask( null );
                }
                taskChain.getChain().clear();
            }
        }
    }

    private boolean isNextTaskLink() {
        return nextTaskLink != null;
    }

    private boolean needToBeSuspendedAfterThis() {
        return this == taskChain.getSuspendAfterTask();
    }

    private boolean isAfterAllToDoTask(TaskLink afterToDo) {
        return afterToDo != null;
    }

    /**
     * Wywołuje kod do wykonania.
     */
    abstract protected void doIt(Object... data);

}
