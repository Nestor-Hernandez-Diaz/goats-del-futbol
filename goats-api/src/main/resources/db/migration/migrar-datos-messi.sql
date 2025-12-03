-- Migrar datos básicos de Messi
USE goats_futbol;

UPDATE players SET 
  hero_info = JSON_OBJECT(
    'birthDate', '24 de junio de 1987',
    'clubs', 'Barcelona, PSG, Inter Miami',
    'quote', 'No juego para ser el mejor de la historia, juego porque amo el fútbol.'
  ),
  profile_image = '../assets/images/messi-profile.png',
  profile_stats = JSON_OBJECT(
    'Nombre completo', 'Lionel Andrés Messi Cuccittini',
    'Altura', '1,70 m',
    'Peso', '72 kg',
    'Pie dominante', 'Izquierdo',
    'Dorsal', '10',
    'Nacimiento', '24 de junio de 1987, Rosario, Argentina'
  ),
  playing_style = JSON_OBJECT(
    'description', 'Lionel Messi es considerado uno de los mejores jugadores de la historia. Su habilidad única para regatear, visión de juego incomparable y capacidad goleadora lo hacen un jugador completo.',
    'attributes', JSON_OBJECT(
      'Regate', 98,
      'Visión', 95,
      'Finalización', 96,
      'Pase', 94,
      'Tiro libre', 92,
      'Aceleración', 90
    )
  ),
  stats = JSON_OBJECT(
    'goals', '800+',
    'assists', '350+',
    'matches', '1000+',
    'titles', '42'
  ),
  gallery = JSON_ARRAY(
    JSON_OBJECT('url', '../assets/images/messi-barcelona.jpg', 'alt', 'Messi Barcelona', 'caption', 'Celebrando con el Barcelona'),
    JSON_OBJECT('url', '../assets/images/messi-argentina.jpg', 'alt', 'Messi Argentina', 'caption', 'Con la selección argentina'),
    JSON_OBJECT('url', '../assets/images/messi-mundial.jpg', 'alt', 'Messi Mundial', 'caption', 'Levantando la Copa del Mundo 2022')
  ),
  legacy = JSON_OBJECT(
    'text', 'El impacto de Lionel Messi en el fútbol mundial es incalculable. No solo ha roto todos los récords imaginables, sino que ha redefinido lo que significa ser un futbolista de élite.',
    'quotes', JSON_ARRAY(
      JSON_OBJECT('text', 'Messi es el mejor jugador que he visto en mi vida', 'author', 'Arrigo Sacchi'),
      JSON_OBJECT('text', 'Es de otro planeta', 'author', 'Zinedine Zidane'),
      JSON_OBJECT('text', 'El mejor de todos los tiempos', 'author', 'Pep Guardiola')
    )
  ),
  videos = JSON_ARRAY(
    JSON_OBJECT('url', 'https://youtube.com/watch?v=video1', 'thumbnail', '../assets/videos/messi-mundial-thumb.jpg', 'title', 'Messi en la final del Mundial 2022'),
    JSON_OBJECT('url', 'https://youtube.com/watch?v=video2', 'thumbnail', '../assets/videos/messi-getafe-thumb.jpg', 'title', 'Gol histórico vs Getafe'),
    JSON_OBJECT('url', 'https://youtube.com/watch?v=video3', 'thumbnail', '../assets/videos/messi-madrid-thumb.jpg', 'title', 'Hat-trick vs Real Madrid')
  )
WHERE id = 1;
