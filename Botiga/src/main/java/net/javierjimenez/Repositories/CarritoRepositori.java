package net.javierjimenez.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.javierjimenez.Models.Carrito;

public interface CarritoRepositori extends MongoRepository<Carrito, String> {

	public List<Carrito> findByUsername(String username);
    public long countByUsername(String username);
    public Carrito findByIdAndUsername(String id, String username);

}
