
--SELECT TA.employeeID as 'employee-id', ifnull(TB.firstNAME||' '||TB.lastNAME,'') as 'name' 
--FROM TB_ATTENDEE TA LEFT JOIN TB_EMPLOYEE TB 
--ON TA.employeeID = TB.employeeID 
--WHERE TA.meetID ='1'

--SELECT meetID as 'meeting-id', meetDATE as date,
--				 startTIME as 'start-time', endTime as 'end-time', 
--			 roomID as 'room-id', description FROM TB_MEETING WHERE 
--			 substr(meetDATE,5,4)||substr(meetDATE,0,3)||substr(meetDATE,3,2) between '20160101' and '20171231'

--SELECT * FROM TB_ATTENDEE

--SELECT roomID, startTIME, endTIME FROM (
--  SELECT roomID, 
--  case when length(startTIME) = 4 then '0'||startTime
--  else startTIME end as startTIME,  
--  case when length(endTime) = 4 then '0'||endTime
--  else endTime end as endTime, meetID 
--  FROM TB_MEETING TA WHERE meetDATE = '11242016'
--  and exists (SELECT 'X' FROM TB_ATTENDEE WHERE TA.meetID = meetID and employeeID = 'bob099')
--) WHERE time(startTIME) between time('11:53') and time('12:59') or   
--        time(endTIME)   between time('11:53') and time('12:59') or
 --       time('12:59')  between time(startTIME) and time(endTIME) 
 
-- SELECT startDATE, endDATE FROM TB_VACATION 
-- WHERE employeeID = 'bob099' and 
-- '20161128' between substr(startDATE,5,4)||substr(startDATE,0,3)||substr(startDATE,3,2)
--                  and     substr(endDATE,5,4)||substr(endDATE,0,3)||substr(endDATE,3,2)

SELECT meetID FROM TB_MEETING TA
WHERE substr(meetDATE,5,4)||substr(meetDATE,0,3)||substr(meetDATE,3,2)
      between '20160101' and '20170101'     
      and exists (SELECT 'X' FROM TB_ATTENDEE WHERE TA.meetID = meetID and employeeID = 'bob099')
      
      
DELETE FROM TB_ATTENDEE
