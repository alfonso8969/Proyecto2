package alfonso8969.com.proyecto2.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Creado por alfonso en fecha 14/10/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH;

    static {
        DB_PATH = "/data/data/alfonso8969.com.proyecto2/databases/";
    }

    private static String DB_NAME;

    static {
        DB_NAME = "Empresa.sqlite";
    }

    private SQLiteDatabase myDatabase;

    private final Context myContext;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read;

        if(!dbExist){
            db_Read = this.getReadableDatabase();
            db_Read.close();
            try {
                copyDataBase();
            } catch (IOException e){
                throw new Error("Error copiando base de datos");
            }
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB;

        try {
            String myPath = DB_PATH+DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch (Exception e){
            File dbFile = new File(DB_PATH+DB_NAME);
            return dbFile.exists();
        }

        if(checkDB != null)
            checkDB.close();

        return checkDB != null;

    }

    private void copyDataBase() throws  IOException{
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new  byte[1024];
        int length;

        while((length = myInput.read(buffer))!= -1){
            if(length>0){
                myOutput.write(buffer,0,length);
            }
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDatabase() throws SQLException{
        String myPath =DB_PATH + DB_NAME;
        try {
            myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLException sqlEX){
            throw new Error("Error abriendo base de datos");
        }
    }

    public synchronized void close(){

        if(myDatabase != null)
            myDatabase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try{
            createDataBase();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Cursor fetchAllEmpleados() throws SQLException{

            Cursor cursor = myDatabase.rawQuery("SELECT * FROM Empleados ORDER BY Nombre ASC",null);
            if(cursor != null)
                cursor.moveToFirst();
            return cursor;
        }

    public  Cursor fetchEmpleadoById(String empleadoId) throws  SQLException{

            Cursor cursor;
            cursor = myDatabase.rawQuery("SELECT * FROM Empleados WHERE _id=?",new String[]{empleadoId});
            if(cursor != null) {
            cursor.moveToFirst();
            }
            return cursor;
    }

    public  Cursor fetchAllNombresEmpleados() throws  SQLException{

        Cursor cursor;
        cursor =  myDatabase.rawQuery("SELECT _id,Nombre,Apellido FROM Empleados ORDER BY Nombre ASC",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllClientes() throws SQLException{

        Cursor cursor = myDatabase.rawQuery("SELECT * FROM Clientes ORDER BY NombreCompania ASC",null);
        if(cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
    public  Cursor fetchClienteById(String clienteId) throws  SQLException{

        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT * FROM Clientes WHERE _id=?",new String[]{clienteId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchDatosClienteByClienteID(String clienteID){
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT NombreCompania,Direccion,Ciudad,Region,CodigoPostal,Pais FROM Clientes WHERE ClienteId=?",new String[]{clienteID});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllClienteID() throws  SQLException{

        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,ClienteID FROM Clientes ORDER BY ClienteID ASC",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

   /* public Cursor fetchIdClienteByClienteID(String clienteID){
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id FROM Clientes WHERE ClienteID=?",new String[]{clienteID});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchDescuentosForProductosByProductoId(String productoId) throws  SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT Descuento FROM PedidosDetalles where ProductoId=?",new String[]{productoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }*/
    public Cursor fetchAllProveedores() throws SQLException{

        Cursor cursor = myDatabase.rawQuery("SELECT * FROM Proveedores ORDER BY NombreCompania ASC",null);
        if(cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
    public  Cursor fetchProveedorById(String proveedorId) throws SQLException{

        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT * FROM Proveedores WHERE _id=?",new String[]{proveedorId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllProductos() throws SQLException{

        Cursor cursor = myDatabase.rawQuery("SELECT * FROM Productos ORDER BY NombreProducto ASC",null);
        if(cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public  Cursor fetchProductoById(String productoId) throws SQLException{

        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT * FROM Productos WHERE _id=?",new String[]{productoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchNombreProveedor(String proveedorId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT NombreCompania FROM Proveedores WHERE _id=?",new String[]{proveedorId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllNombresCompañiaEnvio() throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,NombreCompania FROM Envios ORDER BY NombreCompania ASC",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_idByNombreCompaniaEnvios(String nombreCompania) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id FROM Envios WHERE NombreCompania=?",new String[]{nombreCompania});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchNombreCategoria(String categoriaId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT NombreCategoria FROM Categoria WHERE _id=?",new String[]{categoriaId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_idNombresProducto(String productoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT NombreProducto FROM Productos WHERE _id=?",new String[]{productoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchIdProductoByNombreProducto(String nombreProducto) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id FROM Productos WHERE NombreProducto=?",new String[]{nombreProducto});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchDatosProductosByNombreProducto(String nombreProducto) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT PrecioPorUnidad,UndadesEnPedido,UnidadesEnStock FROM Productos WHERE NombreProducto=?",new String[]{nombreProducto});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllNombresProductos() throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,NombreProducto FROM Productos ORDER BY NombreProducto ASC",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllNombresProveedores() throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,NombreCompania FROM Proveedores",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllNombresCategorias() throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,NombreCategoria FROM Categoria",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_idByNombresProveedor(String nombreCompania) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id FROM Proveedores WHERE NombreCompania=?",new String[]{nombreCompania});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_idByNombresCategoria(String nombreCategoria) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id FROM Categoria WHERE NombreCategoria=?",new String[]{nombreCategoria});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllEnvios() throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT * FROM Envios ORDER BY NombreCompania ASC",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public  Cursor fetchEnvioById(String envioId) throws SQLException{

        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT * FROM Envios WHERE _id=?",new String[]{envioId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllPedidos() throws SQLException{

        Cursor cursor = myDatabase.rawQuery("SELECT * FROM Pedidos ORDER BY PedidoID ASC",null);
        if(cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public  Cursor fetchFechaEntregaPedidoById(String pedidoId) throws SQLException{

        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT FechaEntrega FROM Pedidos WHERE PedidoID=?",new String[]{pedidoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public  Cursor fetchPedidoById(String pedidoId) throws SQLException{

        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT * FROM Pedidos WHERE PedidoID=?",new String[]{pedidoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchLastPedidoID() throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT PedidoID FROM Pedidos WHERE _id = (SELECT MAX(_id) FROM Pedidos)",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchLineasDePedido(String pedidoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT COUNT(PedidoId) FROM PedidosDetalles WHERE PedidoId =? GROUP BY PedidoId HAVING COUNT(PedidoId )>=1",new String[]{pedidoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchIdEmpleadoByNombre(String nombreEmpleado) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id FROM Empleados WHERE Nombre=?",new String[]{nombreEmpleado});
        if (cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchNombreEmpleadoById(String empeadoId) throws SQLException{

        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT Nombre,Apellido FROM Empleados WHERE _id=?",new String[]{empeadoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchNombreCompañiaEnvioById(String envioId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT NombreCompania FROM Envios WHERE _id=?",new String[]{envioId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllProductoIdForPedidosDetallesPedidoId(String pedidoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,productoId FROM PedidosDetalles WHERE PedidoId=?",new String[]{pedidoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchCantidadByPedidoId(String productoId, String pedidoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,Cantidad FROM PedidosDetalles where productoId="+productoId+" and PedidoID=?",new String[]{pedidoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchPrecioUnidadByProductoIdAndPedidoId(String productoId, String pedidoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,PrecioUnidad FROM PedidosDetalles where productoId="+productoId+" and PedidoID=?",new String[]{pedidoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchPrecioUnidadByProductoId(String productoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT PrecioPorUnidad FROM Productos where _Id=?",new String[]{productoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchDescuentoByPedidoId(String productoId,String pedidoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT Descuento FROM PedidosDetalles where productoId="+productoId+" and PedidoID=?",new String[]{pedidoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor calculoSumaTotalPedido(String pedidoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT ROUND(SUM(Cantidad*PrecioUnidad)-(SUM (Cantidad*PrecioUnidad*Descuento)),2)  FROM PedidosDetalles where pedidoId=?",new String[]{pedidoId});
       if(cursor != null) {
           cursor.moveToFirst();
       }
        return cursor;
    }

    //Calcula el 20% del la suma de las lineas del pedido
    public Cursor calculoSumaTotalPrecioFlete(String pedidoId)throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT Round(SUM (( Cantidad*PrecioUnidad*20/100)),2) FROM PedidosDetalles where pedidoId=?",new String[]{pedidoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchPrecioFleteByPedidoId(String pedidoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT PrecioFlete FROM Pedidos WHERE PedidoID=?",new String[]{pedidoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }



    public Cursor fetchUnidadesEnPedidoFromProductosByProductoId(String productoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT UndadesEnPedido From Productos where _id=?",new String[]{productoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public Cursor fetchUnidadesEnStockFromProductosByProductoId(String productoId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT UnidadesEnStock From Productos where _id=?",new String[]{productoId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllPedidosClienteByClienteIDAndFechaEntregaNull(String clienteId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,PedidoID FROM Pedidos WHERE ClienteID=? and FechaEntrega IS null",new String[]{clienteId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllNumOfPedidosClienteByClienteIDAndFechaEntregaNull(String clienteId) throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT COUNT(PedidoID) FROM Pedidos WHERE ClienteID=? and FechaEntrega IS null",new String[]{clienteId});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchAllBarCodes() throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT * FROM CodigoBarras ORDER BY NombreProducto ",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchAllNombreProductoFromBarCodes() throws SQLException{
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT _id,NombreProducto FROM CodigoBarras ORDER BY NombreProducto ",null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchCodigoBarrasByNombreProducto(String nombreProducto) {
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT * FROM CodigoBarras WHERE NombreProducto=? ",new String[]{nombreProducto});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchScanFormatByCodigoBarras(String strCodigoBarras) {
        Cursor cursor;
        cursor = myDatabase.rawQuery("SELECT Formato FROM CodigoBarras WHERE CodigoBarras=? ",new String[]{strCodigoBarras});
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}



