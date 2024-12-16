package com.example.superadmin;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class VerMapa extends AppCompatActivity {

    private MapView mapView;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuración inicial de osmdroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.repartidor_mapa); // Usa el diseño que proporcionaste

        mapView = findViewById(R.id.map);

        // Obtener datos del intent
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        if (latitude == 0.0 || longitude == 0.0) {
            Log.e("VerMapa", "Latitud o longitud inválidas: Latitude=" + latitude + ", Longitude=" + longitude);
            return; // Si los valores no son válidos, no inicializamos el mapa
        }

        // Inicializar y configurar el mapa
        initMap();

        // Colocar el marcador en la posición especificada
        setMarker(latitude, longitude);
    }

    private void initMap() {
        // Configuración del mapa
        mapView.setTileSource(TileSourceFactory.MAPNIK); // Usamos tiles de OpenStreetMap
        mapView.setMultiTouchControls(true); // Permitir gestos multitáctiles como zoom
        mapView.getController().setZoom(17.0); // Nivel de zoom inicial
    }

    private void setMarker(double lat, double lon) {
        // Crear una nueva posición con las coordenadas recibidas
        GeoPoint geoPoint = new GeoPoint(lat, lon);

        // Centrar el mapa en la posición
        mapView.getController().setCenter(geoPoint);

        // Crear y personalizar el marcador
        Marker marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getDrawable(R.drawable.baseline_add_location_24)); // Ícono del marcador
        marker.setTitle("Ubicación del pedido"); // Texto al tocar el marcador

        // Agregar el marcador al mapa
        mapView.getOverlays().add(marker);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Llamar al comportamiento predeterminado (volver al anterior Activity)
        finish(); // Finalizar la actividad actual para liberar recursos
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDetach(); // Limpieza del mapa para evitar fugas de memoria
        }
    }
}
