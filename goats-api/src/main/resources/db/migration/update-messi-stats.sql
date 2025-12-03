USE goats_futbol;

-- Actualizar estadísticas de resumen de Messi
UPDATE players SET 
stats = JSON_OBJECT(
  'goals', 820,
  'assists', 375,
  'matches', 1038,
  'titles', 42
)
WHERE id = 1;

-- Actualizar estadísticas por temporada de Messi (algunas temporadas representativas)
UPDATE players SET 
season_stats = JSON_ARRAY(
  JSON_OBJECT('season', '2004-05', 'club', 'Barcelona', 'matches', 9, 'goals', 1, 'assists', 0),
  JSON_OBJECT('season', '2005-06', 'club', 'Barcelona', 'matches', 25, 'goals', 8, 'assists', 4),
  JSON_OBJECT('season', '2006-07', 'club', 'Barcelona', 'matches', 36, 'goals', 17, 'assists', 3),
  JSON_OBJECT('season', '2007-08', 'club', 'Barcelona', 'matches', 40, 'goals', 16, 'assists', 13),
  JSON_OBJECT('season', '2008-09', 'club', 'Barcelona', 'matches', 51, 'goals', 38, 'assists', 18),
  JSON_OBJECT('season', '2009-10', 'club', 'Barcelona', 'matches', 53, 'goals', 47, 'assists', 11),
  JSON_OBJECT('season', '2010-11', 'club', 'Barcelona', 'matches', 55, 'goals', 53, 'assists', 24),
  JSON_OBJECT('season', '2011-12', 'club', 'Barcelona', 'matches', 60, 'goals', 73, 'assists', 29),
  JSON_OBJECT('season', '2012-13', 'club', 'Barcelona', 'matches', 50, 'goals', 60, 'assists', 16),
  JSON_OBJECT('season', '2013-14', 'club', 'Barcelona', 'matches', 46, 'goals', 41, 'assists', 14),
  JSON_OBJECT('season', '2014-15', 'club', 'Barcelona', 'matches', 57, 'goals', 58, 'assists', 27),
  JSON_OBJECT('season', '2015-16', 'club', 'Barcelona', 'matches', 49, 'goals', 41, 'assists', 23),
  JSON_OBJECT('season', '2016-17', 'club', 'Barcelona', 'matches', 52, 'goals', 54, 'assists', 16),
  JSON_OBJECT('season', '2017-18', 'club', 'Barcelona', 'matches', 54, 'goals', 45, 'assists', 18),
  JSON_OBJECT('season', '2018-19', 'club', 'Barcelona', 'matches', 50, 'goals', 51, 'assists', 19),
  JSON_OBJECT('season', '2019-20', 'club', 'Barcelona', 'matches', 44, 'goals', 31, 'assists', 27),
  JSON_OBJECT('season', '2020-21', 'club', 'Barcelona', 'matches', 47, 'goals', 38, 'assists', 14),
  JSON_OBJECT('season', '2021-22', 'club', 'PSG', 'matches', 34, 'goals', 11, 'assists', 15),
  JSON_OBJECT('season', '2022-23', 'club', 'PSG', 'matches', 41, 'goals', 21, 'assists', 20),
  JSON_OBJECT('season', '2023-24', 'club', 'Inter Miami', 'matches', 19, 'goals', 11, 'assists', 5)
)
WHERE id = 1;

SELECT 'Estadísticas de Messi actualizadas correctamente' as resultado;
