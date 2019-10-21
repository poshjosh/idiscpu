SELECT feedid AS a1, author AS a2, categories AS a3, content AS a4, datecreated AS a5, description AS a6, extradetails AS a7, feeddate AS a8, imageurl AS a9, keywords AS a10, rawid AS a11, timemodified AS a12, title AS a13, url AS a14, siteid AS a15 FROM feed 
WHERE (
  (
    (
      (url = ?) 
      OR (
        (NOT ((siteid IS NULL)) AND (siteid = ?)) AND ((title IS NOT NULL) AND (title = ?))
      )
    ) 
    OR (
      (NOT ((siteid IS NULL)) AND (siteid = ?)) AND ((description IS NOT NULL) AND (description = ?))
    )
  ) 
  OR (
    (NOT ((siteid IS NULL)) AND (siteid = ?)) AND ((content IS NOT NULL) AND (content = ?))
  )
) 
LIMIT ?, ?
