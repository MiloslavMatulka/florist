package com.engeto.florist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Florist {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Florist.class.getName());

        Flower snowflake = new Flower("Bledule", "bílá",
                "jarní květina", true);
        Flower marguerite = new Flower("Kopretina", "bíložlutá",
                "letní květina", false);
        Flower[] flowers = {snowflake, marguerite};

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306",
                "mysql.florist",
                "flor54iSt")) {
            Statement statement = connection.createStatement();
            String table = "florist.flower";
            statement.executeUpdate("DELETE FROM " + table);
            for (Flower flower : flowers) {
                statement.executeUpdate("INSERT INTO " + table
                        + " VALUES " + "(" + flower.getId() + ", '"
                        + flower.getFlowerName() + "', '"
                        + flower.getColor() + "', '"
                        + flower.getDescription() + "', "
                        + flower.isPoisonous()
                        + ")");
            }

            String snowflakeDescriptionUpdate =
                    "Pozor na cibulku - obsahuje největší koncentraci jedu!";
            statement.executeUpdate("UPDATE " + table + " SET "
                    + "description = '" + snowflakeDescriptionUpdate
                    + "' WHERE flower_name = '"
                    + snowflake.getFlowerName() + "'");
            snowflake.setDescription(snowflakeDescriptionUpdate);

            statement.executeUpdate("DELETE FROM " + table
                    + " WHERE is_poisonous = false");
            for (int i = 0; i < flowers.length; ++i) {
                if (!flowers[i].isPoisonous()) {
                    flowers[i] = null;
                }
            }

            statement.executeQuery("SELECT * FROM " + table);
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                System.out.println(result.getString("flower_name"));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Chyba při komunikaci s databází: "
                    + e.getLocalizedMessage());
        }
    }
}
