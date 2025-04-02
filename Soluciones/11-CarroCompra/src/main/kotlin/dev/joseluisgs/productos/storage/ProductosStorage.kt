package dev.joseluisgs.productos.storage

import dev.joseluisgs.common.storage.FileStorage
import dev.joseluisgs.productos.error.ProductoError
import dev.joseluisgs.productos.model.Producto

interface ProductosStorage : FileStorage<List<Producto>, ProductoError>