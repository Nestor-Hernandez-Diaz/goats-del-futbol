#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script para validar las traducciones de clases CSS
"""

import re
import os

def extract_css_classes(css_file_path):
    """Extrae todas las clases CSS de un archivo"""
    classes = set()
    
    try:
        with open(css_file_path, 'r', encoding='utf-8') as file:
            content = file.read()
            
        # Buscar todas las clases CSS (que empiecen con .)
        class_pattern = r'\.([a-zA-Z][a-zA-Z0-9_-]*)'
        matches = re.findall(class_pattern, content)
        
        for match in matches:
            classes.add(match)
            
    except Exception as e:
        print(f"Error leyendo archivo CSS: {e}")
        
    return sorted(list(classes))

def extract_html_classes(html_file_path):
    """Extrae todas las clases usadas en un archivo HTML"""
    classes = set()
    
    try:
        with open(html_file_path, 'r', encoding='utf-8') as file:
            content = file.read()
            
        # Buscar atributos class en HTML
        class_pattern = r'class=["\']([^"\']*)["\']'
        matches = re.findall(class_pattern, content)
        
        for match in matches:
            # Dividir por espacios para obtener clases individuales
            individual_classes = match.split()
            for cls in individual_classes:
                if cls.strip():
                    classes.add(cls.strip())
                    
    except Exception as e:
        print(f"Error leyendo archivo HTML {html_file_path}: {e}")
        
    return sorted(list(classes))

def check_spanish_classes(classes):
    """Verifica qué clases están en español"""
    spanish_indicators = [
        'navegacion', 'contenedor', 'seccion', 'jugador', 'estadistica',
        'hero', 'introduccion', 'tarjeta', 'imagen', 'titulo', 'subtitulo',
        'descripcion', 'destacado', 'logros', 'biografia', 'perfil',
        'galeria', 'videos', 'legado', 'pie', 'boton', 'atributo'
    ]
    
    spanish_classes = []
    english_classes = []
    
    for cls in classes:
        is_spanish = any(indicator in cls.lower() for indicator in spanish_indicators)
        if is_spanish:
            spanish_classes.append(cls)
        else:
            # Verificar si no es una clase técnica (como active, visible, etc.)
            technical_classes = ['active', 'visible', 'jpg', 'line1', 'line2', 'line3']
            if cls not in technical_classes:
                english_classes.append(cls)
    
    return spanish_classes, english_classes

def main():
    print("=== VALIDADOR DE TRADUCCIONES CSS ===\n")
    
    # Rutas de archivos
    css_file = "css/styles.css"
    html_files = [
        "index.html",
        "pages/messi.html", 
        "pages/neymar.html",
        "pages/ronaldo.html"
    ]
    
    # Analizar CSS
    print("1. Analizando clases en CSS...")
    css_classes = extract_css_classes(css_file)
    css_spanish, css_english = check_spanish_classes(css_classes)
    
    print(f"   Total clases en CSS: {len(css_classes)}")
    print(f"   Clases en español: {len(css_spanish)}")
    print(f"   Clases en inglés: {len(css_english)}")
    
    if css_english:
        print(f"   Clases que aún están en inglés:")
        for cls in css_english[:10]:  # Mostrar solo las primeras 10
            print(f"     - {cls}")
        if len(css_english) > 10:
            print(f"     ... y {len(css_english) - 10} más")
    
    # Analizar HTML
    print(f"\n2. Analizando clases en archivos HTML...")
    all_html_classes = set()
    
    for html_file in html_files:
        if os.path.exists(html_file):
            html_classes = extract_html_classes(html_file)
            all_html_classes.update(html_classes)
            html_spanish, html_english = check_spanish_classes(html_classes)
            print(f"   {html_file}:")
            print(f"     Total: {len(html_classes)}, Español: {len(html_spanish)}, Inglés: {len(html_english)}")
    
    # Análisis general
    all_html_spanish, all_html_english = check_spanish_classes(list(all_html_classes))
    
    print(f"\n=== RESUMEN GENERAL ===")
    print(f"CSS - Español: {len(css_spanish)}/{len(css_classes)} ({len(css_spanish)/len(css_classes)*100:.1f}%)")
    print(f"HTML - Español: {len(all_html_spanish)}/{len(all_html_classes)} ({len(all_html_spanish)/len(all_html_classes)*100:.1f}%)")
    
    # Verificar consistencia
    print(f"\n3. Verificando consistencia CSS-HTML...")
    css_set = set(css_classes)
    html_set = all_html_classes
    
    missing_in_css = html_set - css_set
    unused_in_html = css_set - html_set
    
    if missing_in_css:
        print(f"   ⚠️  Clases en HTML que NO están en CSS ({len(missing_in_css)}):")
        for cls in sorted(missing_in_css)[:5]:
            print(f"     - {cls}")
        if len(missing_in_css) > 5:
            print(f"     ... y {len(missing_in_css) - 5} más")
    
    if unused_in_html:
        print(f"   ℹ️  Clases en CSS que NO se usan en HTML ({len(unused_in_html)}):")
        for cls in sorted(unused_in_html)[:5]:
            print(f"     - {cls}")
        if len(unused_in_html) > 5:
            print(f"     ... y {len(unused_in_html) - 5} más")
    
    if not missing_in_css and not unused_in_html:
        print("   ✅ Todas las clases están sincronizadas entre CSS y HTML")
    
    print(f"\n=== ESTADO DE LA TRADUCCIÓN ===")
    total_classes = len(css_classes) + len(all_html_classes)
    total_spanish = len(css_spanish) + len(all_html_spanish)
    percentage = (total_spanish / total_classes * 100) if total_classes > 0 else 0
    
    print(f"Progreso total: {total_spanish}/{total_classes} clases traducidas ({percentage:.1f}%)")
    
    if percentage >= 90:
        print("🎉 ¡Excelente! La mayoría de las clases están en español")
    elif percentage >= 70:
        print("👍 Buen progreso en la traducción")
    elif percentage >= 50:
        print("⚠️  Traducción en progreso, faltan algunas clases")
    else:
        print("❌ Aún quedan muchas clases por traducir")

if __name__ == "__main__":
    main()