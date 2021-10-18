package com.dev4lazy.pricecollector.utils;

public abstract class TaskLink {

    private TaskChain taskChain;
    private TaskLink nextTaskLink;

    // TODO !!! usuń konstruktor, bo rozbiłes metodę setTaskChain, która jest wołana w TaskChain
    //  i nie trzeba nic ektra robić. Po usunieciu konstruktora trzeba będzie skorygowac użyci
    //  TaskLink w zapsie cen na ekranie 5
    /*public TaskLink(TaskChain taskChain) {
        this.taskChain = taskChain;
    }

     */

    protected void setTaskChain( TaskChain taskChain ) {
        this.taskChain = taskChain;
    }

    public TaskChain getTaskChain() {
        return taskChain;
    }

    /* TODO XXX
    private TaskLink getNextTaskLink() {
        return nextTaskLink;
    }
     */

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
                    // TODO XXX nextTaskLink.takeData(data);
                    nextTaskLink.doIt( data );
                }
            } else {
                TaskLink afterAllToDo = taskChain.getAfterAllToDoTask();
                if (isAfterAllToDoTask(afterAllToDo)) {
                    // TODO XXX afterToDo.takeData(data);
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
     * Odbiera dane potrzebne do wykonania, od poprzedniego TaskLink.
     *
     * @param data (Object...) - dane do odebrania od poprzedniego TaskLink.
     */
    // TODO XXX abstract protected void takeData(Object... data);

    /**
     * Wywołuje kod do wykonania.
     */
    abstract protected void doIt(Object... data);

}
