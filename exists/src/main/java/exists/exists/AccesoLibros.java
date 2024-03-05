package exists.exists;

import org.xmldb.api.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;

public class AccesoLibros {
    private Collection col = null;
    private XPathQueryService servicio;
    
    public void conectar() {
        String driver = "org.exist.xmldb.DatabaseImpl";
        col = null;

        // String uri="xmldb:exist://192.168.1.16:8080/exist/xmlrpc/db";//ip TAMBIÉN FUNCIONA
        // String uri="xmldb:exist://embedded-eXist-server"; //si tenemos la bdd embebida
        String uri = "xmldb:exist://localhost:8080/exist/xmlrpc/db";
        String user = "admin";
        String pass = "1234";

        try {
            // NOS CONECTAMOS
            Class cl = Class.forName(driver);
            System.out.println("Driver encontrado para " + driver);
            Database db = (Database) cl.newInstance();
            System.out.println("Db instancia creada");
            DatabaseManager.registerDatabase(db);
            System.out.println("BDD registrada");

            // nos conectamos con el método getCollection
            // LA VARIABLE COL ESTÁ "ASIGNADA" Y LA PUEDEN USAR EL RESTO DE MÉTODOS
            col = DatabaseManager.getCollection(uri, user, pass);
            System.out.println("Conectados a la bdd " + uri);

            // COMPROBAMOS SI LA CONEXIÓN TIENE RECURSOS (ARCHIVOS O COLECCIONES)
            if (col == null) {
                System.out.println("---colección no encontrada ---");
            } else {
                System.out.println("Nº de recursos de la colección:" + col.getResourceCount());
                System.out.println("Primer recurso:" + col.listResources()[0]); // muestra el primer recurso

                // podríamos hacer un bucle para mostrar todos los recursos
                // desde i=0 hasta col.getResourceCount()
            }

            // OBTENEMOS EL SERVICIO Y LE ASIGNAMOS VALOR
            servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desconectar() {
        try {
            col.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consultarTitulos() {
        try {
            ResourceSet result = servicio.query("for $b in //Titulo/text() return $b");
            
            // VISUALIZAMOS EL RESULTADO DE LA CONSULTA
            System.out.println("Numero de resultados:" + result.getSize());
            ResourceIterator it = result.getIterator();

            while (it.hasMoreResources()) {
                Resource r = (Resource) it.nextResource();
                System.out.println((String) r.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
