package org.factory;

import org.dao.*;

public abstract class DAOFactory {

    public abstract EstudianteDAO getEstudianteDAO();

    public abstract CarreraDAO getCarreraDAO();

    public abstract InscripcionDAO getInscripcionDAO();

    // ...puedes agregar otros DAOs si el proyecto lo requiere...
}

