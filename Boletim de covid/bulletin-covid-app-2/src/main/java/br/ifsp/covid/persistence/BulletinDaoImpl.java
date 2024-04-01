package br.ifsp.covid.persistence;

import br.ifsp.covid.model.Bulletin;
import br.ifsp.covid.model.State;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BulletinDaoImpl implements BulletinDao{
    @Override
    public void insert(Bulletin bulletin) {
        String sql = """
                INSERT INTO bulletin(city,state,infected,deaths,icu_ratio,date)
                VALUES(?,?,?,?,?,?)
                """;
        try {
            PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql);
            stmt.setString(1,bulletin.getCity());
            stmt.setString(2,String.valueOf(bulletin.getState()));
            stmt.setInt(3,bulletin.getInfected());
            stmt.setInt(4,bulletin.getDeaths());
            stmt.setDouble(5,bulletin.getIcuRatio());
            stmt.setString(6,String.valueOf(bulletin.getDate()));
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM bulletin WHERE id = ?";
        try {
            PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql);
            stmt.setInt(1,id);
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Bulletin bulletin) {
        String sql = """
                UPDATE bulletin SET city = ?,state = ?,infected = ?,
                deaths = ?,icu_ratio = ?,date = ? WHERE id=?
                """;
        try {
            PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql);
            stmt.setString(1,bulletin.getCity());
            stmt.setString(2,String.valueOf(bulletin.getState()));
            stmt.setInt(3,bulletin.getInfected());
            stmt.setInt(4,bulletin.getDeaths());
            stmt.setDouble(5,bulletin.getIcuRatio());
            stmt.setString(6,String.valueOf(bulletin.getDate()));
            stmt.setInt(7,bulletin.getId());
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Bulletin> findAll() {
        String sql = "SELECT * FROM bulletin";
        List<Bulletin> list = new ArrayList<>();
        try {
            PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String city = rs.getString("city");
                State state = State.fromName(rs.getString("state"));
                int infected = rs.getInt("infected");
                int deaths = rs.getInt("deaths");
                double icuRatio = rs.getDouble("icu_ratio");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                Bulletin bulletin = new Bulletin(id,city,state,infected,deaths,icuRatio,date);
                list.add(bulletin);
            }
            return list;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
