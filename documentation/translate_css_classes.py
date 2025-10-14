#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script para traducir clases CSS del inglés al español
"""

import re
import os
import shutil
from datetime import datetime

# Diccionario de traducción de clases CSS de inglés a español
CLASS_TRANSLATIONS = {
    # Navegación
    'main-nav': 'navegacion-principal',
    'nav-container': 'contenedor-navegacion',
    'nav-links': 'enlaces-navegacion',
    'burger-menu': 'menu-hamburguesa',
    'burger-toggle': 'toggle-hamburguesa',
    'line1': 'linea1',
    'line2': 'linea2', 
    'line3': 'linea3',
    'logo': 'logotipo',
    'active': 'activo',
    
    # Contenedores y layout
    'container': 'contenedor',
    'section-title': 'titulo-seccion',
    'section-description': 'descripcion-seccion',
    'section': 'seccion',
    
    # Hero section
    'hero-section': 'seccion-hero',
    'hero-content': 'contenido-hero',
    'hero-title': 'titulo-hero',
    'hero-subtitle': 'subtitulo-hero',
    'hero-overlay': 'superposicion-hero',
    
    # Intro section
    'intro-section': 'seccion-introduccion',
    'intro-content': 'contenido-introduccion',
    'intro-text': 'texto-introduccion',
    'intro-stats': 'estadisticas-introduccion',
    
    # Stats/Estadísticas
    'stat-item': 'elemento-estadistica',
    'stat-number': 'numero-estadistica',
    'stat-label': 'etiqueta-estadistica',
    'stat-card': 'tarjeta-estadistica',
    'stats-table': 'tabla-estadisticas',
    
    # Players section
    'players-section': 'seccion-jugadores',
    'players-container': 'contenedor-jugadores',
    'player-card': 'tarjeta-jugador',
    'player-image': 'imagen-jugador',
    'player-name': 'nombre-jugador',
    'player-nickname': 'apodo-jugador',
    'player-highlights': 'destacados-jugador',
    'player-description': 'descripcion-jugador',
    'player-link': 'enlace-jugador',
    'player-page': 'pagina-jugador',
    'player-content': 'contenido-jugador',
    
    # Player hero sections
    'player-hero-section': 'seccion-hero-jugador',
    'player-hero-content': 'contenido-hero-jugador',
    'player-hero-text': 'texto-hero-jugador',
    'player-hero-title': 'titulo-hero-jugador',
    'player-hero-subtitle': 'subtitulo-hero-jugador',
    'player-hero-image': 'imagen-hero-jugador',
    'player-hero-info': 'info-hero-jugador',
    'player-hero-quote': 'cita-hero-jugador',
    
    # Specific player pages
    'messi-hero': 'hero-messi',
    'ronaldo-hero': 'hero-ronaldo',
    'neymar-hero': 'hero-neymar',
    'messi-page': 'pagina-messi',
    'ronaldo-page': 'pagina-ronaldo',
    'neymar-page': 'pagina-neymar',
    
    # Card elements
    'card-image-container': 'contenedor-imagen-tarjeta',
    'card-content': 'contenido-tarjeta',
    'country-flag': 'bandera-pais',
    
    # Highlights and achievements
    'highlight': 'destacado',
    'highlight-event': 'evento-destacado',
    'highlight-year': 'año-destacado',
    'highlights-list': 'lista-destacados',
    'career-highlights': 'destacados-carrera',
    
    # Achievements
    'achievements-section': 'seccion-logros',
    'achievements-content': 'contenido-logros',
    'achievements-list': 'lista-logros',
    'achievements-clubs': 'logros-clubes',
    'achievements-individual': 'logros-individuales',
    'achievements-national': 'logros-nacionales',
    'club-achievements': 'logros-clubes',
    
    # Biography
    'biography-section': 'seccion-biografia',
    'biography-content': 'contenido-biografia',
    'biography-text': 'texto-biografia',
    'biography-sidebar': 'barra-lateral-biografia',
    
    # Profile
    'player-profile-card': 'tarjeta-perfil-jugador',
    'profile-image': 'imagen-perfil',
    'profile-details': 'detalles-perfil',
    'profile-stats': 'estadisticas-perfil',
    
    # Info items
    'info-item': 'elemento-info',
    'info-label': 'etiqueta-info',
    'info-value': 'valor-info',
    
    # Statistics
    'statistics-section': 'seccion-estadisticas',
    'statistics-content': 'contenido-estadisticas',
    'statistics-details': 'detalles-estadisticas',
    'statistics-summary': 'resumen-estadisticas',
    
    # Playing style
    'playing-style-section': 'seccion-estilo-juego',
    'style-content': 'contenido-estilo',
    'style-description': 'descripcion-estilo',
    'style-attributes': 'atributos-estilo',
    
    # Attributes
    'attribute': 'atributo',
    'attribute-name': 'nombre-atributo',
    'attribute-value': 'valor-atributo',
    'attribute-bar': 'barra-atributo',
    'attribute-fill': 'relleno-atributo',
    
    # Gallery
    'player-gallery-section': 'seccion-galeria-jugador',
    'gallery-grid': 'cuadricula-galeria',
    'gallery-item': 'elemento-galeria',
    'gallery-caption': 'leyenda-galeria',
    
    # Videos
    'videos-section': 'seccion-videos',
    'video-grid': 'cuadricula-videos',
    'video-item': 'elemento-video',
    'video-thumbnail': 'miniatura-video',
    'video-title': 'titulo-video',
    'play-button': 'boton-reproducir',
    
    # Legacy
    'legacy-section': 'seccion-legado',
    'legacy-content': 'contenido-legado',
    'legacy-text': 'texto-legado',
    'legacy-quotes': 'citas-legado',
    'quote-card': 'tarjeta-cita',
    
    # Footer
    'main-footer': 'pie-principal',
    'footer-content': 'contenido-pie',
    'footer-logo': 'logotipo-pie',
    'footer-links': 'enlaces-pie',
    'footer-resources': 'recursos-pie',
    'footer-newsletter': 'newsletter-pie',
    'footer-bottom': 'pie-inferior',
    'newsletter-form': 'formulario-newsletter',
    
    # Logos
    'club-logo': 'logotipo-club',
    'national-logo': 'logotipo-nacional',
    
    # Buttons
    'btn': 'boton',
    
    # Misc
    'featured-image': 'imagen-destacada',
    'visible': 'visible',
    'jpg': 'jpg'
}

def create_backup():
    """Crea una copia de seguridad de los archivos antes de modificarlos"""
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    backup_dir = f"backup_{timestamp}"
    
    if not os.path.exists(backup_dir):
        os.makedirs(backup_dir)
    
    # Copiar archivos importantes
    files_to_backup = [
        "css/styles.css",
        "index.html",
        "pages/messi.html",
        "pages/neymar.html", 
        "pages/ronaldo.html"
    ]
    
    for file_path in files_to_backup:
        if os.path.exists(file_path):
            backup_path = os.path.join(backup_dir, file_path.replace('/', '_'))
            shutil.copy2(file_path, backup_path)
            print(f"✓ Backup creado: {backup_path}")
    
    return backup_dir

def translate_css_file(css_file_path):
    """Traduce las clases CSS en el archivo especificado"""
    try:
        with open(css_file_path, 'r', encoding='utf-8') as file:
            content = file.read()
        
        original_content = content
        translations_made = []
        
        # Traducir cada clase
        for english_class, spanish_class in CLASS_TRANSLATIONS.items():
            # Buscar y reemplazar selectores de clase
            pattern = r'\.{}\b'.format(re.escape(english_class))
            if re.search(pattern, content):
                content = re.sub(pattern, f'.{spanish_class}', content)
                translations_made.append(f"{english_class} → {spanish_class}")
        
        # Escribir el archivo modificado
        with open(css_file_path, 'w', encoding='utf-8') as file:
            file.write(content)
        
        return translations_made
        
    except Exception as e:
        print(f"Error procesando archivo CSS: {e}")
        return []

def translate_html_file(html_file_path):
    """Traduce las clases CSS en un archivo HTML"""
    try:
        with open(html_file_path, 'r', encoding='utf-8') as file:
            content = file.read()
        
        original_content = content
        translations_made = []
        
        # Traducir clases en atributos class
        for english_class, spanish_class in CLASS_TRANSLATIONS.items():
            # Buscar class="english-class" o class="other-class english-class"
            patterns = [
                rf'class="([^"]*\s)?{re.escape(english_class)}(\s[^"]*)?"',
                rf"class='([^']*\s)?{re.escape(english_class)}(\s[^']*)?'"
            ]
            
            for pattern in patterns:
                matches = re.finditer(pattern, content)
                for match in matches:
                    old_attr = match.group(0)
                    new_attr = old_attr.replace(english_class, spanish_class)
                    content = content.replace(old_attr, new_attr)
                    if f"{english_class} → {spanish_class}" not in translations_made:
                        translations_made.append(f"{english_class} → {spanish_class}")
        
        # Escribir el archivo modificado
        with open(html_file_path, 'w', encoding='utf-8') as file:
            file.write(content)
        
        return translations_made
        
    except Exception as e:
        print(f"Error procesando archivo HTML {html_file_path}: {e}")
        return []

def main():
    print("=== TRADUCTOR DE CLASES CSS AL ESPAÑOL ===\n")
    
    # Crear backup
    print("1. Creando backup de archivos...")
    backup_dir = create_backup()
    print(f"✓ Backup completado en: {backup_dir}\n")
    
    # Traducir archivo CSS
    print("2. Traduciendo archivo CSS...")
    css_translations = translate_css_file("css/styles.css")
    print(f"✓ CSS traducido: {len(css_translations)} clases modificadas")
    for translation in css_translations[:10]:  # Mostrar solo las primeras 10
        print(f"   {translation}")
    if len(css_translations) > 10:
        print(f"   ... y {len(css_translations) - 10} más")
    print()
    
    # Traducir archivos HTML
    print("3. Traduciendo archivos HTML...")
    html_files = [
        "index.html",
        "pages/messi.html",
        "pages/neymar.html", 
        "pages/ronaldo.html"
    ]
    
    total_html_translations = 0
    for html_file in html_files:
        if os.path.exists(html_file):
            html_translations = translate_html_file(html_file)
            total_html_translations += len(html_translations)
            print(f"✓ {html_file}: {len(html_translations)} clases modificadas")
        else:
            print(f"✗ Archivo no encontrado: {html_file}")
    
    print(f"\n=== RESUMEN ===")
    print(f"✓ Clases CSS traducidas: {len(css_translations)}")
    print(f"✓ Total traducciones en HTML: {total_html_translations}")
    print(f"✓ Backup guardado en: {backup_dir}")
    print(f"✓ Traducción completada exitosamente!")
    
    print(f"\n=== DICCIONARIO DE TRADUCCIONES APLICADAS ===")
    print(f"Total de traducciones definidas: {len(CLASS_TRANSLATIONS)}")

if __name__ == "__main__":
    main()