package br.ifsp.covid.persistence;

import br.ifsp.covid.model.Bulletin;

import java.util.List;

public interface BulletinDao {
    void insert(Bulletin bulletin);
    void delete(int id);
    void update(Bulletin bulletin);
    List<Bulletin> findAll();
}
