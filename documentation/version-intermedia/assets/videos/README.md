# Carpeta de Videos

Esta carpeta contiene todos los archivos de video utilizados en el sitio web GOATS Fútbol.

## Contenido Recomendado

### Highlights (`highlights/`)
- **Nombre**: `highlight-[jugador]-[evento]-[año].mp4`
- **Ejemplo**: `highlight-messi-mundial-2022.mp4`, `highlight-ronaldo-champions-2017.mp4`
- **Duración**: 15-60 segundos
- **Resolución**: 1280x720px (720p)
- **Formato**: MP4 (H.264)
- **Bitrate**: 2-4 Mbps
- **Peso máximo**: 10MB
- **Cantidad necesaria**: 3-5 highlights por jugador

### Entrevistas (`interviews/`)
- **Nombre**: `interview-[jugador]-[tema]-[año].mp4`
- **Ejemplo**: `interview-neymar-carrera-2023.mp4`
- **Duración**: 30-120 segundos
- **Resolución**: 1280x720px (720p)
- **Formato**: MP4 (H.264)
- **Bitrate**: 2-4 Mbps
- **Peso máximo**: 20MB
- **Cantidad necesaria**: 1-2 entrevistas por jugador
- **Alternativa**: Enlaces a YouTube para entrevistas completas

### Videos de Fondo (`background/`)
- **Nombre**: `bg-video-[seccion].mp4`
- **Ejemplo**: `bg-video-home.mp4`, `bg-video-estadio.mp4`
- **Duración**: 10-30 segundos (loops)
- **Resolución**: 1920x1080px (1080p)
- **Formato**: MP4 (H.264)
- **Bitrate**: 3-5 Mbps
- **Peso máximo**: 15MB
- **Cantidad necesaria**: 1-2 videos de fondo para secciones principales

## Especificaciones Técnicas

### Formato y Codificación
- **Contenedor**: MP4
- **Codec de Video**: H.264 (Alta compatibilidad)
- **Codec de Audio**: AAC, 128-192 Kbps, estéreo
- **Framerate**: 30fps (estándar) o 60fps (movimiento fluido)

### Optimización
- **Compresión**: Usar herramientas como Handbrake o Adobe Media Encoder
- **Precarga**: Configurar atributo `preload="metadata"` en HTML
- **Poster**: Proporcionar imagen de poster para cada video (JPG, mismo nombre que el video)
- **Subtítulos**: Incluir archivos .vtt para accesibilidad cuando sea necesario

### Implementación
- **Videos cortos**: Alojar directamente en el sitio
- **Videos largos**: Utilizar incrustaciones de YouTube/Vimeo
- **Reproducción automática**: Solo para videos de fondo, sin sonido
- **Controles**: Incluir controles para todos los videos de contenido

## Consideraciones de Rendimiento
- Implementar carga diferida (lazy loading)
- Ofrecer versiones de menor calidad para conexiones lentas
- Limitar la cantidad de videos por página
- Considerar el uso de GIF o secuencias de imágenes para clips muy cortos