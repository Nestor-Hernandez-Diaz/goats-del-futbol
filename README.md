# 🐐 GOATs del Fútbol

Una wiki interactiva dedicada a los **Greatest Of All Time** del fútbol mundial: **Lionel Messi**, **Cristiano Ronaldo** y **Neymar Jr**.

## 🌟 Características

- **Diseño Responsive**: Optimizado para dispositivos móviles, tablets y desktop
- **Navegación Intuitiva**: Interfaz limpia con navegación por pestañas
- **Perfiles Detallados**: Información completa de cada jugador
- **Galería Interactiva**: Imágenes de alta calidad con efectos hover
- **Comparación de Estadísticas**: Tabla comparativa entre los tres jugadores
- **Línea de Tiempo**: Historia y logros cronológicos
- **Animaciones Suaves**: Efectos CSS para una experiencia fluida

## 🚀 Tecnologías Utilizadas

- **HTML5**: Estructura semántica y accesible
- **CSS3 Avanzado**: Flexbox, Grid, Pseudoclases y Pseudoelementos (::before, ::after), Animaciones (@keyframes), Transiciones, Transformaciones (translate, scale), Variables CSS (Custom Properties)
- **JavaScript**: Interactividad y funcionalidades dinámicas
- **Responsive Design**: Media queries para adaptabilidad

## 📁 Estructura del Proyecto

```
proyecto-goats-futbol/
├── assets/
│   ├── icons/          # Iconos SVG y PNG
│   ├── images/         # Imágenes de jugadores y equipos
│   └── videos/         # Recursos de video
├── css/
│   └── styles.css      # Estilos consolidados
├── js/
│   └── main.js         # Funcionalidades JavaScript
├── pages/
│   ├── messi.html      # Página de Lionel Messi
│   ├── ronaldo.html    # Página de Cristiano Ronaldo
│   └── neymar.html     # Página de Neymar Jr
├── audits/             # Reportes de auditoría
└── index.html          # Página principal
```

## 🎯 Funcionalidades

### Página Principal
- Hero section con imagen de fondo
- Navegación por pestañas entre jugadores
- Sección de comparación estadística
- Galería de imágenes interactiva
- Línea de tiempo de logros

### Páginas Individuales
- **Messi**: Biografía, carrera en Barcelona, PSG e Inter Miami
- **Ronaldo**: Trayectoria en Real Madrid, Juventus y Al-Nassr
- **Neymar**: Historia en Santos, Barcelona, PSG y Al-Hilal

### Características Técnicas
- CSS consolidado para mejor rendimiento
- Animaciones CSS keyframes
- Sistema de grid responsive
- Efectos hover y transiciones suaves

## 🛠️ Instalación y Uso

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/Nestor-Hernandez-Diaz/goats-del-futbol.git
   ```

2. **Navegar al directorio**:
   ```bash
   cd goats-del-futbol
   ```

3. **Abrir en servidor local**:
   ```bash
   # Con Python
   python -m http.server 8000
   
   # Con PHP
   php -S localhost:8000
   
   # O simplemente abrir index.html en el navegador
   ```

4. **Acceder al proyecto**:
   - Abrir `http://localhost:8000` en tu navegador

## 📱 Responsive Design

El proyecto está optimizado para:
- **Desktop**: 1200px+
- **Tablet**: 768px - 1199px
- **Mobile**: 320px - 767px

## 🎨 Paleta de Colores

- **Primario**: `#1a1a2e` (Azul oscuro)
- **Secundario**: `#16213e` (Azul medio)
- **Acento**: `#0f3460` (Azul profundo)
- **Dorado**: `#ffd700` (Destacados)
- **Texto**: `#ffffff` (Blanco)

## 🏆 Jugadores Destacados

### 🇦🇷 Lionel Messi
- 8 Balones de Oro
- Copa del Mundo 2022
- 4 Champions League
- Máximo goleador histórico del Barcelona

### 🇵🇹 Cristiano Ronaldo
- 5 Balones de Oro
- 5 Champions League
- Eurocopa 2016
- Máximo goleador de la historia

### 🇧🇷 Neymar Jr
- Medalla de Oro Olímpica 2016
- Champions League 2015
- Copa América 2019
- Estrella del fútbol brasileño

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

**Nestor Hernandez Diaz**
- GitHub: [@Nestor-Hernandez-Diaz](https://github.com/Nestor-Hernandez-Diaz)

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para cambios importantes:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

⭐ **¡No olvides dar una estrella al proyecto si te gustó!** ⭐

## 🧠 Técnicas avanzadas de CSS (ubicación y uso)

- **Grid**: Layout principal asimétrico (2fr/1fr) en páginas de jugadores; cuadrículas en estadísticas, logros y galería con `repeat(auto-fill, minmax(...))`.
- **Flexbox**: Header y navegación (`space-between`); hero centrado (`align-items`, `justify-content`); tarjetas de jugadores con `flex-wrap`; columnas verticales en biografía (`flex-direction: column`).
- **Pseudoclases**: `:hover` en tarjetas e imágenes; `:nth-child(even)` en filas de tablas; estados activos en navegación.
- **Pseudoelementos**: `::after` para línea dorada bajo títulos; `::before` para comillas decorativas en citas.
- **Animaciones (@keyframes)**: `glow` (brillo pulsante) y `fadeIn` (entrada suave) en elementos destacados y tarjetas.
- **Transiciones**: Uso de `--transition-normal` para enlaces, botones, imágenes y overlays en galería.
- **Transformaciones**: `translateY` para elevación en hover; `scale` para zoom; `translate(-50%, -50%)` para centrado absoluto de botones de video.
- **Variables CSS**: Design tokens en `:root` para colores, tipografía y espaciados, reutilizados en todo el proyecto.
- **Posicionamiento avanzado**: Header `sticky` siempre visible; overlays de video con `position: absolute` para cobertura total.
- **Media Queries**: Ajustes de grid y flex para tablet (≤768px) y mobile (≤480px), optimizando legibilidad y disposición.

## 📚 Documentación y recursos

- Guion de presentación del CSS comentado: <mcfile name="GUION_PRESENTACION_CSS_COMENTADO.md" path="C:\xampp\htdocs\proyecto-goats-futbol\documentation\GUION_PRESENTACION_CSS_COMENTADO.md"></mcfile>
- Técnicas avanzadas de CSS (detalle y ubicaciones): <mcfile name="TECNICAS_AVANZADAS_CSS.md" path="C:\xampp\htdocs\proyecto-goats-futbol\documentation\TECNICAS_AVANZADAS_CSS.md"></mcfile>
- Hoja de estilos comentada: <mcfile name="styles-comentado.css" path="C:\xampp\htdocs\proyecto-goats-futbol\css\styles-comentado.css"></mcfile>