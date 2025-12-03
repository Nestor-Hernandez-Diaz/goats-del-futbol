USE goats_futbol;

-- Actualizar galería de Messi con las 6 imágenes completas (como en messi.html)
UPDATE players SET 
gallery = JSON_ARRAY(
  JSON_OBJECT(
    'url', '../assets/images/messi-barcelona.jpg',
    'alt', 'Lionel Messi celebrando un gol emblemático con la camiseta azulgrana del FC Barcelona - Era dorada en el Camp Nou',
    'caption', 'Messi celebrando un gol con el Barcelona'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-copa-mundial.jpg',
    'alt', 'Lionel Messi levantando la Copa del Mundo FIFA 2022 en Qatar - Momento histórico de máxima gloria',
    'caption', 'Messi levantando la Copa del Mundo 2022'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-copa-america.jpg',
    'alt', 'Lionel Messi celebrando con el trofeo de la Copa América 2021 en Brasil - Primer título con Argentina',
    'caption', 'Messi con la Copa América 2021'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-balon-oro.jpg',
    'alt', 'Lionel Messi posando con su octavo Balón de Oro 2023 - Récord histórico de galardones individuales',
    'caption', 'Messi con su octavo Balón de Oro'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-psg.jpg',
    'alt', 'Lionel Messi en acción con la camiseta del Paris Saint-Germain - Nueva etapa en su carrera',
    'caption', 'Messi con el PSG'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-inter-miami.jpg',
    'alt', 'Lionel Messi celebrando con Inter Miami CF en la MLS - Impacto revolucionario en el fútbol estadounidense',
    'caption', 'Messi con el Inter Miami'
  )
)
WHERE id = 1;

SELECT 'Galería de Messi actualizada con 6 imágenes' as resultado;
