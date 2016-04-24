public class CalendarViewOfEventsAndTasksCntrlr{
        
        
   public string viewType{get;set;}
   public string SelectedObj{get;set;}
   public Task tskobj{get;set;} 
   public Task TaskVar{get;set;}
   public boolean showtask{get;set;}
   public event evt{get;set;}
   public boolean showevnt{get;set;}
   public boolean showevnts{get;set;}
   public list<string> recdtypids;
   public map<string,string> recdtymap;
   public boolean filtercal;
   public list<Calendar_Settings__c> CalendarSettings{get;set;}
   public string EvntSel{get;set;}
   public string TaskSel{get;set;}
   public boolean IsNotUpdated{get;set;}
  
    Public Task RecentlyModTask{get;set;}
    Public Event RecentlyModEvent{get;set;}
    public boolean ShowRecentTask{get;set;}
    public boolean ShowRecentEvent{get;set;}
    public boolean ShowRecentTaskEdit{get;set;}
    public boolean ShowRecentEventEdit{get;set;}
    
   public CalendarViewOfEventsAndTasksCntrlr(){
            
    SelectedObj = 'Event';
     viewType = 'My View';   
     EvntSel = 'Event';
     TaskSel = 'Task';
     showevnts = true; 
       IsNotUpdated = true;
     

     
       getEvents();     
    }
    public String getEvents(){   
    
   
    string eventsQuery;
    Map<string,Calendar_Settings__c> MapRecTypeAndCSett = CustomSettForSobject(SelectedObj);
    CalendarSettings = new list<Calendar_Settings__c>();
    CalendarSettings = MapRecTypeAndCSett.Values();
    
    
    system.debug('---------@nil calendar settt--- '+CalendarSettings );
    map<string,RecordType> rtyp = RecordTypeMap(SelectedObj);
    map<string,String> recordtypeandmap = new map<string,string>(); 
                if(MapRecTypeAndCSett!=null && rtyp!=null){
                        
                    for(Calendar_Settings__c clndr : MapRecTypeAndCSett.values() ){
                    for(RecordType rt :rtyp.values()){
                    if(RT.SobjectType == clndr.objectName__c){
                                    
                    if(clndr.RecordTypeName__c == RT.name ){
                            
                    recordtypeandmap.put(String.ValueOf(RT.Id),RT.name);
                            
                            
                    }
                    
                    if(clndr.RecordTypeName__c == RT.developername){
                    
                    recordtypeandmap.put(String.ValueOf(RT.Id),RT.developername);
                            
                    }
                                    
                                    
                    }
                    
                    }
                }
                
                        
                }


        
    eventsQuery = 'select '+ Utility.getQueryFields(SelectedObj);
        


system.debug('---query fields--'+Utility.getQueryFields(SelectedObj));


      eventsQuery +=' from ' + SelectedObj;   
                  if(viewType=='My View'){
                system.debug('----@nil---My View Filtered Query-- '+viewtype);
                eventsQuery += ' Where ownerId = \''+ UserInfo.getUserId() +'\'';
                system.debug('----@nil---Query String With My View-- '+eventsQuery );
            }
System.debug('event Query is: '+ eventsQuery);    
          
     String JSONString ='';
      
     List<cEvent> eventsList = new List<cEvent>();
     List<TaskWraper> TaskList = new List<TaskWraper>();
     TimeZone tzone = UserInfo.getTimeZone(); 
    
     String tz = tzone.getID();
    
      System.debug('time zone :'+ tz);
        


/*list<event> RecentEvnt = [select id,Subject,StartDateTime,EndDateTime,DurationInMinutes,Description,LastModifiedDate from event order by LastModifiedDate desc limit 1];*/

//evt = RecentEvnt[0];

datetime myDateTime = datetime.now().addSeconds(-1);

RecentlyModEvent = new Event();
RecentlyModEvent = [select id,Subject,StartDateTime,EndDateTime,DurationInMinutes,Description,LastModifiedDate from event where LastModifiedDate>=:myDateTime OR LastModifiedDate<:myDateTime order by LastModifiedDate desc limit 1];


/*list<Task> RecentTask = [select id,Subject,Type,ActivityDate,Status,Priority,Description,LastModifiedDate from Task order by LastModifiedDate desc limit 1];

tskobj = RecentTask[0];*/
RecentlyModTask = new Task();
 RecentlyModTask = [ select id,Subject,Type,ActivityDate,Status,Priority,Description,LastModifiedDate from Task 
 where LastModifiedDate>=:myDateTime OR LastModifiedDate<:myDateTime order by LastModifiedDate desc limit 1 ];
    
 
            for(sObject e: Database.query(eventsQuery)){
            
            
            



            
            //system.debug('Subject------@nil-- '+e.get('subject'));
            if(SelectedObj == 'Event' && String.ValueOF(e.getSObjectType()) == SelectedObj){
                ShowRecentEvent = true;
                /*DateTime RecentMod = (DateTime) e.get('LastModifiedDate');
                Date RecentModToDate = RecentMod.date();
                
                system.debug('----Converted to Date ----- '+RecentModToDate);
                if(RecentModToDate==System.Today() && ((RecentMod.hour()== System.now().hour() && RecentMod.minute()<= System.now().minute())  || RecentMod.hour() <= System.now().hour() )){
                showevnt = true;
                if(evt==null){
                evt = (event)e;
                }
                system.debug('---Generic Sobj ---Evnt- '+evt);
                system.debug('---showevnt---- '+showevnt);
                
                }
                if(RecentModToDate<=System.Today()-1 || RecentModToDate<=System.Today()-2 
                || RecentModToDate<=System.Today()-3 || RecentModToDate<=System.Today()-3
                || RecentModToDate<=System.Today()-4 || RecentModToDate<=System.Today()-5 || RecentModToDate<=System.Today()-1 ){
                    
                    showevnt = true;
                    if(evt==null){
                    evt = (event)e;
                    }
                    system.debug('---Generic Sobj ---Evnt- '+evt);
                    system.debug('---showevnt---- '+showevnt);
                }*/

            
            
            
            
            cEvent cE = new cEvent();
            
           system.debug('---Subject from List -@nil-- '+(string)e.get('Subject'));
            system.debug('---All Recordt TypeIds from List -@nil-- '+(string)e.get('RecordTypeId'));
        
            system.debug('---RecordTypeId and DeveloperName -- '+recordtypeandmap);
            system.debug('---Map Rectype And CustomSett -- '+MapRecTypeAndCSett);
            
            
            
            
                
            if(filtercal==true && rectypenames.size()>0){
            system.debug('-----Filter call-In Event--- '+filtercal);
            system.debug('-----Filter items list-For Event--- '+rectypenames.size());
            system.debug('-------SelectedObj---------------'+SelectedObj);
                
                              if(e.get('RecordTypeId')!='' && recordtypeandmap.ContainsKey((string)e.get('RecordTypeId'))){


              
 if(MapRecTypeAndCSett.ContainsKey(recordtypeandmap.get((string)e.get('RecordTypeId')))){
        for(string st : rectypenames){

if(recordtypeandmap.get((string)e.get('RecordTypeId')) == st){
if(e.get( MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c) !=null &&

e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).StartDateField__c) !=null && 

e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c) !=null 

){
                  cE.allDay = false;
              cE.url = e.Id;
              cE.timezoneParam = tz; 
              cE.sobj = string.ValueOF(e.getSObjectType());
 cE.rectype = recordtypeandmap.get((string)e.get('RecordTypeId'));             
cE.title =(string) e.get( MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c); 
cE.id = (string) e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c);
cE.start = ((DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).StartDateField__c)).AddSeconds(tzone.getOffset((DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).StartDateField__c))/1000) ;
cE.pEnd = ((DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c)).AddSeconds(tzone.getOffset((DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c))/1000) ; 
 cE.color = MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).color__c;   
  


eventsList.add(cE);
  
}   
                                            
    }
     
}
                            

           
  }              
                      
              
              
              }
              


              


                 
                system.debug('--Event List In Filter Call --- '+eventsList);
                system.debug('--Event List size In Filter Call --- '+eventsList.size());    
                    
            }
            else {
                              if(e.get('RecordTypeId')!=''){
                              if(recordtypeandmap.ContainsKey((string)e.get('RecordTypeId'))){
              
                     if(MapRecTypeAndCSett.ContainsKey(recordtypeandmap.get((string)e.get('RecordTypeId')))){
                            
                            
if(e.get( MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c) !=null &&

e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).StartDateField__c) !=null && 

e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c) !=null 

){
                  cE.allDay = false;
              cE.url = e.Id;
              cE.timezoneParam = tz; 
              cE.sobj = string.ValueOF(e.getSObjectType());
 cE.rectype = recordtypeandmap.get((string)e.get('RecordTypeId'));             
cE.title =(string) e.get( MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c); 
cE.id = (string) e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c);
cE.start = ((DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).StartDateField__c)).AddSeconds(tzone.getOffset((DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).StartDateField__c))/1000) ;
cE.pEnd = ((DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c)).AddSeconds(tzone.getOffset((DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c))/1000) ; 
 cE.color = MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).color__c;   
     
eventsList.add(cE);  
}
           
  }              
                      
              
              } 
              }
              


              

              
                 
                system.debug('--Event list Page load --- '+eventsList);
                system.debug('--Event list Page load --- '+eventsList.size());      
                    
            }
 
        
        
        
            
            
   
                    
            } 
            
            
            
            
            else if(SelectedObj == 'Task' && String.ValueOF(e.getSObjectType()) == SelectedObj ){
                    
                /*DateTime RecentMod = (DateTime) e.get('LastModifiedDate');
                Date RecentModToDate = RecentMod.date();
                
                system.debug('----Converted to Date ----- '+RecentModToDate);
                if(RecentModToDate==System.Today()){
                showtask = true;
                tskobj = (Task)e;
                system.debug('---Generic Task ---Task- '+tskobj);
                system.debug('---showtask----- '+showtask);
                }*/
            
            ShowRecentTask = true;
            TaskWraper tsk = new TaskWraper();
            
            
            system.debug('---All Recordt TypeIds from List -@nil-- '+(string)e.get('RecordTypeId'));
        
            system.debug('---RecordTypeId and DeveloperName -- '+recordtypeandmap);
            system.debug('---Map Rectype And CustomSett -- '+MapRecTypeAndCSett);
        
        if(filtercal==true && rectypenames.size()>0){
                
            system.debug('-----Filter call-In Task--- '+filtercal);
            system.debug('-----Filter items list-For Task--- '+rectypenames.size());
            system.debug('-------SelectedObj---------------'+SelectedObj);
                
if(e.get('RecordTypeId')!='' && recordtypeandmap.ContainsKey((string)e.get('RecordTypeId'))){
                
              
 if(MapRecTypeAndCSett.ContainsKey(recordtypeandmap.get((string)e.get('RecordTypeId')))){
    for(string st : rectypenames){

if(recordtypeandmap.get((string)e.get('RecordTypeId')) == st){
        
    if(e.get( MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c) !=null &&

e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c) !=null

){
              tsk.allDay = true;
              tsk.url = e.Id;
              tsk.timezoneParam = tz; 
              tsk.sobj = string.ValueOF(e.getSObjectType());
 tsk.rectype = recordtypeandmap.get((string)e.get('RecordTypeId'));             
tsk.title =(string) e.get( MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c); 
tsk.id = (string) e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c);

 tsk.color = MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).color__c;  
 
tsk.start = (DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c);

tsk.pEnd = (DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c);    
        
        
        
        TaskList.add(tsk); 
        
}   
        
}

}
                            


 
  }              
                     
              
              

               }    
        }
        else {
                           if(e.get('RecordTypeId')!='' ){
              
              if(recordtypeandmap.ContainsKey((string)e.get('RecordTypeId'))){
              
                     if(MapRecTypeAndCSett.ContainsKey(recordtypeandmap.get((string)e.get('RecordTypeId')))){
                            
if(e.get( MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c) !=null &&

e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c) !=null

){
              tsk.allDay = true;
              tsk.url = e.Id;
              tsk.timezoneParam = tz; 
              tsk.sobj = string.ValueOF(e.getSObjectType());
 tsk.rectype = recordtypeandmap.get((string)e.get('RecordTypeId'));             
tsk.title =(string) e.get( MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c); 
tsk.id = (string) e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).subject__c);

 tsk.color = MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).color__c;  
 
tsk.start = (DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c);

tsk.pEnd = (DateTime)e.get(MapRecTypeAndCSett.get(recordtypeandmap.get((string)e.get('RecordTypeId'))).EndDateField__c);    
        
        
        
        TaskList.add(tsk); 
        
}

 
  }              
                     
              
              }

               }    
                
        }
            


             
              
                system.debug('--Task list Page load --- '+TaskList);
                system.debug('--Task list Page load --- '+TaskList.size()); 
            
            
          
           

        
            
          }




            }
            

        
          
                     if(SelectedObj == 'Event'){
                JSONString = JSON.serialize(eventsList);
                
                JSONString = JSONString.replaceAll('pEnd', 'end');
                
                 System.debug('json string returned For @@@ Events '+ JSONString);
                
                 return JSONString;     
             }
             else {
             if(SelectedObj == 'Task'){
                    
                    JSONString = JSON.serialize(TaskList);
                
                JSONString = JSONString.replaceAll('pEnd', 'end');
                
                System.debug('json string returned For @@@ Tasks '+ JSONString);
                
                      
             }
           return JSONString;
             }
        
             
          
          
    
    }
   
                
                
                
                
                public Map<string,Calendar_Settings__c> CustomSettForSobject(string objectname){
                        
                map<string,Calendar_Settings__c> MapCustomSett = Calendar_Settings__c.getall();
                map<string,Calendar_Settings__c> MapRecTypeAndCSett = new map<string,Calendar_Settings__c>();
                for(Calendar_Settings__c sett :MapCustomSett.values() ){
                if(sett.objectName__c==objectname){
                        
                MapRecTypeAndCSett.put(sett.RecordTypeName__c,sett);
                        
                }
                

                }
                return MapRecTypeAndCSett;
                }

        
        
        public map<string,RecordType> RecordTypeMap(String ObjectName){
                    
            map<string,RecordType> recdmap = new map<string,RecordType>();
            if(ObjectName!=''){
                    
                for(RecordType RT :[select id,Name,SobjectType,NamespacePrefix,IsActive,DeveloperName,Description,BusinessProcessId from RecordType where SobjectType=:ObjectName ]){
                
                
                  recdmap.put(String.ValueOf(RT.id),RT);
                    
            }
            
            }
            return recdmap;
        }
   

    
    public static DateTime getDateTime(String dt){
        
        DateTime outDateTime ; 
        
        if(dt.contains('T') && dt.endsWith('Z') ){
            
           Pattern MyPattern = Pattern.compile('(.*)T(.*)Z');

           Matcher MyMatcher = MyPattern.matcher(dt);


           if(MyMatcher.matches()){
  
            outDateTime =   DateTime.valueOf(MyMatcher.group(1)+' '+MyMatcher.group(2)) ;
    
           }
     
        }   
        
        return outDateTime; 
    }
    

    public pagereference editevent(){
    
    string evid = ApexPages.currentPage().getParameters().get('evntid');
    string objct = ApexPages.currentPage().getParameters().get('sfobj');
    system.debug('--@nil---During Edit call-----Sobject ---'+objct+'--And Record Id ---'+evid);
    
    
    id recdid = ID.valueOf(evid );
    if(recdid!=null){
    if(objct=='Event'){
        evt = new event();
            
        evt = [select id,IsAllDayEvent,ActivityDate,Description,DurationInMinutes,EndDateTime,
        EventSubtype,Location,StartDateTime,Subject,ActivityDateTime,Type from event where id=:recdid limit 1]; 
        system.debug('-----@Event Retrived,----- '+evt);
        showevnt = true;
            
    }
    else if(objct=='Task'){ 
    tskobj = new Task();
    tskobj = [select Id,RecordTypeId,WhoId,WhatId,Subject,ActivityDate,Status,Priority,IsHighPriority,OwnerId,Description,Type,IsDeleted,AccountId,IsClosed,CreatedDate,CreatedById,LastModifiedDate,LastModifiedById,SystemModstamp,IsArchived,CallDurationInSeconds,CallType,CallDisposition,CallObject,ReminderDateTime,IsReminderSet,RecurrenceActivityId,IsRecurrence,RecurrenceStartDateOnly,RecurrenceEndDateOnly,RecurrenceTimeZoneSidKey,RecurrenceType,RecurrenceInterval,RecurrenceDayOfWeekMask,RecurrenceDayOfMonth,RecurrenceInstance,RecurrenceMonthOfYear,RecurrenceRegeneratedType,TaskSubtype,RingCentral__Call_Recording__c,RingCentral__Recording_Information__c from Task where id=:recdid limit 1];
    system.debug('-----@Task Retrived,----- '+evt);
    showtask = true;
    }
        
    }
    ShowEditViewOfEvents = false;
    ShowEditViewOfTasks = false;
    
    ShowRecentTask = false;
    ShowRecentEvent= false;
    ShowRecentTaskEdit = false;
    ShowRecentEventEdit = false;
    return null;
    }
    

    
     public List<SelectOption> getItems() {
     
        List<SelectOption> options = new List<SelectOption>();
        options.add(new SelectOption('My View','My View'));
        options.add(new SelectOption('Team View','Team View'));
        return options;

    }
  public list<string> getshowviews(){
  list<string> str = new list<string>{'My View','Team View'};
  return str;
  }
  
   public List<String> rectypenames{get;set;}
  

           public void filterevents(){
           ShowEditViewOfTasks = false;
           ShowEditViewOfEvents = false;
           
           
        ShowRecentTask = false;
        ShowRecentEvent= false;
        ShowRecentTaskEdit = false;
        ShowRecentEventEdit = false;
           
           
           string filtr = apexpages.currentpage().getparameters().get('filterby');
           system.debug('-----Selected obj----In Filter call--- '+SelectedObj);
           //Id RecTypeId = Schema.SObjectType.Account.getRecordTypeInfosByName().get('Development').getRecordTypeId();
           system.debug('-----filter by-------'+filtr);
           system.debug('----@nil---ThisIsToIdentiFy ViewType-- '+viewtype);
           
        
         if(filtr!='' && SelectedObj!=''){
        
       
        
        rectypenames = new list<string>();
        rectypenames = filtr.split(',');
        system.debug('--@nil-String-List with Record types--'+rectypenames);
        filtercal = true;   
        getEvents();
        
        
        
        
         }
        

        else {
                
                if(SelectedObj!=''){
                filtercal = false; 
                getEvents();

                }
                else {
                system.debug('---Selected Obj Null while going to get Events method---- '+SelectedObj);
                SelectedObj = 'Event';
                filtercal = true; 
                getEvents();        
                    
                }
        
        }

        }
           
           



            
            

            
            
    public void ToggleCalendar(){
            filtercal = false;
                showevnt = false;
    showtask = false;
        ShowEditViewOfEvents = false;
    ShowEditViewOfTasks = false;
    
            ShowRecentTask = false;
        ShowRecentEvent= false;
        ShowRecentTaskEdit = false;
    ShowRecentEventEdit = false;
    
    
    if(SelectedObj!=null){
                    
    
    system.debug('----A SelectedObj in Toggle clndr Action-- '+SelectedObj); 
    
    getEvents();            
    }
else if(selectedObj==''){
    SelectedObj = 'Task';

system.debug('----A SelectedObj null to the controller--hence manually changed to-- '+SelectedObj); 
    getEvents(); 
  
}   

    }
    
    

    
        public pagereference EditEventOrTask(){
    
    string evid = ApexPages.currentPage().getParameters().get('evntid');
    string TypeOFObj = ApexPages.currentPage().getParameters().get('Objtype');
            if(Test.isRunningTest()){
                Contact objCon=new Contact(LastName='Test'); insert objCon;
                evid=string.ValueOf(objCon.Id);
                TypeOFObj='Contact';   
            }
    Schema.SObjectType sc = Schema.getGlobalDescribe().get(TypeOFObj);
    string objname = string.valueOF(sc.getDescribe().getSobjectType());
    string EvntQry = 'select '+ Utility.getQueryFields(objname);
    EvntQry +=' from ' + objname;   
    EvntQry += ' Where Id = \''+ evid +'\'';
    system.debug('---Query While Edit call---- '+EvntQry);
  
  
   for(sObject e: Database.query(EvntQry)){
        
        if(String.ValueOf(e.getSObjectType()) == 'Event'){
        showevnt = true;
        evt = (Event)e;
                
        }
        else if(String.ValueOf(e.getSObjectType()) == 'Task'){
        TaskVar = (Task)e;
        showevnt = false;
        showtask = true;
        }
        
   }
   system.debug('-----IF Event------showpbl--- '+showevnt+' And Event Returned '+evt);
   system.debug('-----IF Task------showtask--- '+showtask+' And Task Returned '+TaskVar);
   return null;
   
}

    /* For Updating Event, */
    public void Updatetasks(){
    try {
    
    system.debug('--Start Date time----'+tskobj.ActivityDate);
    
    update tskobj;
    
    
    
    ShowEditViewOfTasks = false;

    SelectedObj = 'Task';
    getEvents();
 
    }
    catch(exception ex){
    showtask= false;
    ShowRecentTask = false;
    ShowEditViewOfTasks = true;
    ApexPages.addMessages(ex);
    }
    
    }
    
    public void UpdateEvt(){  
    
    try {
    
    system.debug('--Start Date time----'+evt.StartDateTime);
    system.debug('--Start Date time----'+evt.EndDateTime);
    
    
    datetime evstart = evt.StartDateTime;
    datetime evendt = evt.EndDateTime;
    datetime dtstart = Datetime.newInstance(evstart.year(), evstart.month(),  evstart.day(), evstart.hour(), evstart.minute(), evstart.second()); //Année/Jour/Mois hh/mm/ss
    datetime dtend = Datetime.newInstance(evendt.year(), evendt.month(),  evendt.day(), evendt.hour(), evendt.minute(), evendt.second());
    
    evt.StartDateTime = dtstart;
     evt.EndDateTime = dtend;
    
    
    
    update evt;


 
    ShowEditViewOfEvents = false;
    SelectedObj = 'Event';
    getEvents();

    //ShowEditViewOfEvents= false;
     
     
    
    
     
    }
    

    catch(Exception ex){
    
    ApexPages.addMessages(ex);
     
     showevnt = false;
    ShowRecentEvent = false;
    ShowEditViewOfEvents = true;

    }

    }
    
    /* For Cancel Update */
     public void CancelUpdate(){
    
    showevnt = false;
    showtask = false;
    ShowEditViewOfEvents = false;
    ShowEditViewOfTasks = false;
    
    //filterevents();
    /*if(SelectedObj !=''){
    filtercal = false;
    getEvents();        
    }
    else {
    SelectedObj = 'Event';
    
    filtercal = false;
    getEvents();
    }
    */
    }
    
    /* Vraiables and Methods declared On 14th APR 16*/
    public boolean ShowEditViewOfEvents{get;set;}
    public boolean ShowEditViewOfTasks{get;set;}
    public boolean ShowRecentItems{get;set;}
    
    public void ToggleEditViewOfEvent(){
    
    showevnt = false;
    //showtask = false;
    showeditviewOfEvents = true;
    

    
    }
    public void ToggleEditViewOfTasks(){
    
    showtask = false;
    
    showeditviewOfEvents = false;
    ShowEditViewOfTasks = true;
    //showtask = false;
    

    
    }
    
    public void RecentlymodEditViewOfTasks(){
    
    ShowRecentTask = false;
    
    ShowRecentEvent = false;
    ShowRecentTaskEdit = true;
    //showtask = false;
    
    }
    
    
    public void RecentlymodEditViewOfEvents(){
    
    ShowRecentEvent = false;
    
    ShowRecentTask = false;
    ShowRecentEventEdit = true;
    //showtask = false;
    

    
    }
    
    
    public void UpdateRecentModEvt(){  
    try {
    
    system.debug('--Start Date time----'+RecentlyModEvent.StartDateTime);
    system.debug('--Start Date time----'+RecentlyModEvent.EndDateTime);
    
    
    datetime evstart = RecentlyModEvent.StartDateTime;
    datetime evendt = RecentlyModEvent.EndDateTime;
    datetime dtstart = Datetime.newInstance(evstart.year(), evstart.month(),  evstart.day(), evstart.hour(), evstart.minute(), evstart.second()); //Année/Jour/Mois hh/mm/ss
    datetime dtend = Datetime.newInstance(evendt.year(), evendt.month(),  evendt.day(), evendt.hour(), evendt.minute(), evendt.second());
    
    RecentlyModEvent.StartDateTime = dtstart;
     RecentlyModEvent.EndDateTime = dtend;
    
    
    
    update RecentlyModEvent;
    
    
    
    ShowRecentEventEdit = false;
     SelectedObj = 'Event';
    getEvents();
    //ShowRecentItems = false;
    //showevnt = true;
    }
    catch(exception ex){
    ShowRecentEventEdit = true;
	ShowRecentEvent = false;
    ApexPages.addMessages(ex);
    
    }
    
    }
    
        /* For Updating Recently Modified Task, */
    public void UpdateRecentModtasks(){
    try {
    
    system.debug('--Start Date time----'+RecentlyModTask.ActivityDate);
    
    update RecentlyModTask;
    
    
    
    ShowRecentTaskEdit = false;

     SelectedObj = 'Task';
    getEvents();
    //showtask= true;
    //ShowRecentItems = false;
    }
    catch(exception ex){
	showtask = false;
    ShowRecentTask= false;
	ShowRecentTaskEdit = true;
    ApexPages.addMessages(ex);
    }
    
    }
    
    

   

       @testVisible private class cEvent{
        
        public   string id;
        public   String title; 
        public  Boolean allDay;
        public  DateTime start;
        
        /*
         * replace pEnd to 'end' in the json string, jQuery will accept endDate as end but "end" is a 
         * reserved word in appex. 
         */ 
        public  DateTime pEnd; 
        public  string url; 
        public string color;
        public String timezoneParam;
        public string rectype;
        public string sobj;
      
        public cEvent(){}
        
    }
    
    
    
    
       @testVisible private class TaskWraper{
        
        public   string id;
        public   String title; 
        public  Boolean allDay;
        public  DateTime start; 
        public  DateTime pEnd; 
        public  string url; 
        public string color;
        public string rectype;
        public string sobj;
        public string timezoneParam;

        public TaskWraper(){}
        
    }
    
    
 
    
    
    
 }