package com.example.quejapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;
import java.util.Objects;

@Document
public class Queja {
    private @Id
    @GeneratedValue String id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;

    @NotNull(message="La descripción es un campo obligatorio, por favor comentenos su incidente.")
    @Size(min=2,  message="la descripcion debe tener un minimo de información para ser valida.")
    @Size(max=255,  message="la descripcion debe contener maximo 255 caracteres.")
    private String descripcion;

    @NotNull(message="Debe seleccionar el tipo de incidente")
    private Integer tipoQueja;

    @NotNull(message="La ubicación es un campo obligatorio, por favor comentenos donde ocurrio el incidente.")
    @Size(min=2, max=255, message = "la ubicación debe tener un minimo de información para ser valida.")
    private String ubicacion;
    private String usuarioId;
    private Date fechaRespuesta;
    private String encargado;
    private Integer estado;
    private String respuesta;

    public Queja() {
        this.fecha = new Date();
    }
    public Queja(Date fecha, String descripcion, Integer tipoQueja, String ubicacion,String usuarioId) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipoQueja = tipoQueja;
        this.ubicacion = ubicacion;
        this.usuarioId = usuarioId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getTipoQueja() {
        return tipoQueja;
    }

    public void setTipoQueja(Integer tipoQueja) {
        this.tipoQueja = tipoQueja;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(Date fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queja queja = (Queja) o;
        return Objects.equals(id, queja.id) && Objects.equals(fecha, queja.fecha) && Objects.equals(descripcion, queja.descripcion)
                && Objects.equals(tipoQueja, queja.tipoQueja) && Objects.equals(ubicacion, queja.ubicacion)
                && Objects.equals(usuarioId, queja.usuarioId) && Objects.equals(fechaRespuesta, queja.fechaRespuesta)
                && Objects.equals(encargado, queja.encargado)  && Objects.equals(estado, queja.estado)
                && Objects.equals(respuesta, queja.respuesta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, descripcion, tipoQueja, ubicacion, usuarioId, fechaRespuesta, encargado, estado, respuesta);
    }

    @Override
    public String toString() {
        return "Queja{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", TipoQueja=" + tipoQueja +
                ", ubicacion='" + ubicacion + '\'' +
                ", usuarioId=" + usuarioId +
                ", fechaRespuesta=" + fechaRespuesta +
                ", encargado=" + encargado +
                ", estado='" + estado + '\'' +
                ", respuesta='" + respuesta + '\'' +
                '}';
    }
}
