# Запрос 1

```
SELECT COUNT(*) FROM (SELECT profile_id FROM profile 
EXCEPT
SELECT DISTINCT profile_id FROM post) AS X
```

|count|
|-----|
|5|

# Запрос 2

```
SELECT post_id FROM post WHERE
(title LIKE '0%' OR title LIKE '1%' OR title LIKE '2%' OR title LIKE '3%' OR title LIKE '4%' OR title LIKE '5%'
OR title LIKE '6%' OR title LIKE '7%' OR title LIKE '8%' OR title LIKE '9%')
INTERSECT
SELECT DISTINCT post_id FROM comment GROUP BY (post_id) HAVING COUNT(*) = 2
INTERSECT SELECT post_id FROM post WHERE LENGTH(content) > 20 
ORDER BY (post_id)
```

|post_id|
|-------|
|22|
|24|
|26|
|28|
|32|
|34|
|36|
|38|
|42|
|44|

# Запрос 3

```
SELECT post_id from comment GROUP BY (post_id) HAVING COUNT(*) <= 1
ORDER BY (post_id)
LIMIT 10
```

|post_id|
|-------|
|1|
|3|
|5|
|7|
|9|
|11|
|13|
|15|
|17|
|19|
