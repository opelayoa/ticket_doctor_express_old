package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.ProjectStatus;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IProjectStatusManager extends IDatabaseManager {

    ProjectStatus insertProjectStatus(ProjectStatus projectStatus);

    void insertOrReplaceInTxProjectStatus(ProjectStatus... projectStatus);

    ArrayList<ProjectStatus> listProjectStatuses();

    ArrayList<ProjectStatus> listProjectStatusesWithNone(String message);
}
