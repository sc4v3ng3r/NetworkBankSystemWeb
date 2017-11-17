
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author scavenger
 */
public class GerarTabelas {
    public static void main(String[] args) {
        Configuration configs = new AnnotationConfiguration();
        configs.configure();
        SchemaExport schema = new SchemaExport(configs);
        schema.create(true, true);
    }
}
