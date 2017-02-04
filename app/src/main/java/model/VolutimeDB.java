package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Faina0502 on 21/01/2017.
 */

public class VolutimeDB extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "VolutimeDB.db";
    ////////////////////////////////////VOLUNTEER TABLE//////////////////////////////
    public static final String VOLUNTEER_TABLE_NAME = "volunteer";

    public static final String VOLUNTEER_COLUMN_ID  = "volunteerID";
    public static final String VOLUNTEER_COLUMN_EMAIL  = "email";
    public static final String VOLUNTEER_COLUMN_PASSWORD= "password";
    public static final String VOLUNTEER_COLUMN_FNAME  = "fName";
    public static final String VOLUNTEER_COLUMN_LNAME= "lName";
    public static final String VOLUNTEER_COLUMN_ADDRESS= "address";
    public static final String VOLUNTEER_COLUMN_DATEOFBIRTH= "dateOfBirth";
    public static final String VOLUNTEER_COLUMN_VOLUPIC= "voluPic";

    private static final String[] TABLE_VOLUNTEER_COLUMNS = {VOLUNTEER_COLUMN_ID, VOLUNTEER_COLUMN_EMAIL,
            VOLUNTEER_COLUMN_PASSWORD, VOLUNTEER_COLUMN_FNAME, VOLUNTEER_COLUMN_LNAME, VOLUNTEER_COLUMN_ADDRESS,VOLUNTEER_COLUMN_DATEOFBIRTH
            ,VOLUNTEER_COLUMN_VOLUPIC};
    ////////////////////////////////////ORGANIZATION TABLE//////////////////////////////
    public static final String ORGANIZATION_TABLE_NAME = "organization";

    public static final String ORGANIZATION_COLUMN_ID  = "organizationID";
    public static final String ORGANIZATION_COLUMN_NAME  = "name";
    public static final String ORGANIZATION_COLUMN_EMAIL= "email";
    public static final String ORGANIZATION_COLUMN_PASSWORD= "password";
    public static final String ORGANIZATION_COLUMN_ADDRESS= "address";
    public static final String ORGANIZATION_COLUMN_PIC= "orgPic";
    private static final String[] TABLE_ORGANIZATION_COLUMNS = {ORGANIZATION_COLUMN_ID, ORGANIZATION_COLUMN_NAME,
            ORGANIZATION_COLUMN_EMAIL, ORGANIZATION_COLUMN_PASSWORD, ORGANIZATION_COLUMN_ADDRESS,
            ORGANIZATION_COLUMN_PIC};

    /////////////////////////////VOUNTEER_ACTIVITY//////////////////////////////////
    public static final String EVENT_TABLE_NAME = "volunteer_activity";

    public static final String EVENT_COLUMN_ID  = "eventID";
    public static final String EVENT_COLUMN_VOLUNTEER_ID  = "volunteerID";
    public static final String EVENT_COLUMN_ORG_ID= "organizationID";
    public static final String EVENT_COLUMN_DATE= "date";
    public static final String EVENT_COLUMN_START_TIME= "startTime";
    public static final String EVENT_COLUMN_END_TIME= "endTime";
    public static final String EVENT_COLUMN_DETAILS= "details";
    private static final String[] TABLE_EVENT_COLUMNS = {EVENT_COLUMN_ID, EVENT_COLUMN_VOLUNTEER_ID,
            EVENT_COLUMN_ORG_ID, EVENT_COLUMN_DATE, EVENT_COLUMN_START_TIME, EVENT_COLUMN_END_TIME,
            ORGANIZATION_COLUMN_PIC};
    //////////////////////////////////VOLUNTEER_ORG//////////////////////////////
    public static final String VOL_AT_ORG_TABLE_NAME = "volunteer_at_org";

    public static final String VOL_AT_ORG_COLUMN_VOLUNTEER_ID  = "volunteerID";
    public static final String VOL_AT_ORG_COLUMN_ORG_ID= "organizationID";
    public static final String VOL_AT_ORG_COLUMN_START_DATE= "startDate";
    public static final String VOL_AT_ORG_COLUMN_END_DATE= "endDate";
    private static final String[] TABLE_VOL_AT_ORG_COLUMNS = {VOL_AT_ORG_COLUMN_VOLUNTEER_ID, VOL_AT_ORG_COLUMN_ORG_ID,
            VOL_AT_ORG_COLUMN_START_DATE, VOL_AT_ORG_COLUMN_END_DATE};
    //////////////////////////////////MESSAGE///////////////////////////////
    public static final String MESSAGE_TABLE_NAME = "message";

    public static final String MESSAGE_COLUMN_MESSAGE_ID  = "messageID";
    public static final String MESSAGE_COLUMN_SENDER_ID  = "senderID";
    public static final String MESSAGE_COLUMN_RECEIVE_ID= "receiverID";
    public static final String MESSAGE_COLUMN_USERTYPE  = "userType";
    public static final String MESSAGE_COLUMN_DATE= "date";
    public static final String MESSAGE_COLUMN_TIME= "time";
    public static final String MESSAGE_COLUMN_CONTENT= "content";
    public static final String MESSAGE_COLUMN_PARENT_ID= "parentID";
    private static final String[] TABLE_MESSAGE_COLUMNS = {MESSAGE_COLUMN_MESSAGE_ID, MESSAGE_COLUMN_SENDER_ID,
            MESSAGE_COLUMN_RECEIVE_ID, MESSAGE_COLUMN_USERTYPE, MESSAGE_COLUMN_DATE, MESSAGE_COLUMN_TIME
            , MESSAGE_COLUMN_CONTENT, MESSAGE_COLUMN_PARENT_ID};
    ///////////////////////////////USERTYPE////////////////////////////////
    public static final String USERTYPE_TABLE_NAME = "userType";

    public static final String USERTYPE_COLUMN_ID  = "typeID";
    public static final String USERTYPE_COLUMN_TYPE = "userType";

    private static final String[] TABLE_USERTYPE_COLUMNS = {USERTYPE_COLUMN_ID, USERTYPE_COLUMN_TYPE};
    // private static final HashMap hp;
    private SQLiteDatabase db = null;

    private static final String CREATE_TABLE_VOLUNTEER = "create table if not exists " + VOLUNTEER_TABLE_NAME + " ( "
            + VOLUNTEER_COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VOLUNTEER_COLUMN_EMAIL + " TEXT, "
            + VOLUNTEER_COLUMN_PASSWORD+ " TEXT, "
            + VOLUNTEER_COLUMN_FNAME + " TEXT, "
            + VOLUNTEER_COLUMN_LNAME + " TEXT, "
            + VOLUNTEER_COLUMN_ADDRESS + " TEXT, "
            + VOLUNTEER_COLUMN_DATEOFBIRTH + " TEXT, "
            + VOLUNTEER_COLUMN_VOLUPIC + " BLOB)";

    private static final String CREATE_TABLE_ORGANIZATION = "create table if not exists " + ORGANIZATION_TABLE_NAME + " ( "
            + ORGANIZATION_COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ORGANIZATION_COLUMN_NAME+ " TEXT,  "
            + ORGANIZATION_COLUMN_ADDRESS + " TEXT, "
            + ORGANIZATION_COLUMN_EMAIL+ " TEXT, "
            + ORGANIZATION_COLUMN_PASSWORD + " TEXT, "
            + ORGANIZATION_COLUMN_PIC + " BLOB) ";


    private static final String CREATE_TABLE_EVENT = "create table if not exists " + EVENT_TABLE_NAME + " ( "
            + EVENT_COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENT_COLUMN_VOLUNTEER_ID+ " INTEGER,  "
            + " FOREIGN KEY ("+ EVENT_COLUMN_VOLUNTEER_ID +") REFERENCES "+ VOLUNTEER_TABLE_NAME+"("+VOLUNTEER_COLUMN_ID+") "
            + EVENT_COLUMN_ORG_ID+ " INTEGER,  "
            + " FOREIGN KEY ("+ EVENT_COLUMN_ORG_ID +") REFERENCES "+ ORGANIZATION_TABLE_NAME+"("+ ORGANIZATION_COLUMN_ID+")"
            + EVENT_COLUMN_DATE+ " TEXT,  "
            + EVENT_COLUMN_START_TIME+ " TEXT, "
            + EVENT_COLUMN_END_TIME + " TEXT ,"
            + EVENT_COLUMN_DETAILS+ " TEXT)";


    private static final String CREATE_TABLE_VOLUNTEER_ORG = "create table if not exists " + VOL_AT_ORG_TABLE_NAME + " ( "

            + VOL_AT_ORG_COLUMN_VOLUNTEER_ID + " INTEGER PRIMARY KEY,  "
            + " FOREIGN KEY ("+ VOL_AT_ORG_COLUMN_VOLUNTEER_ID +") REFERENCES "+ VOLUNTEER_TABLE_NAME+"("+VOLUNTEER_COLUMN_ID+")"
            + VOL_AT_ORG_COLUMN_ORG_ID+ " INTEGER PRIMARY KEY,  "
            + " FOREIGN KEY ("+ VOL_AT_ORG_COLUMN_ORG_ID +") REFERENCES "+ ORGANIZATION_TABLE_NAME+"("+ ORGANIZATION_COLUMN_ID+")"
            + VOL_AT_ORG_COLUMN_START_DATE+ " TEXT,  "
            + VOL_AT_ORG_COLUMN_END_DATE+ " TEXT)";


    private static final String CREATE_TABLE_USERTYPE = "create table if not exists " + USERTYPE_TABLE_NAME + " ( "

            + USERTYPE_COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERTYPE_COLUMN_TYPE+ " INTEGER)";


    private static final String CREATE_TABLE_MESSAGE = "create table if not exists " + MESSAGE_TABLE_NAME + " ( "

            + MESSAGE_COLUMN_MESSAGE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MESSAGE_COLUMN_SENDER_ID+ " INTEGER,  "
            + MESSAGE_COLUMN_RECEIVE_ID+ " INTEGER,  "
            + MESSAGE_COLUMN_PARENT_ID+ " INTEGER,  "
            + " FOREIGN KEY ("+ MESSAGE_COLUMN_PARENT_ID +") REFERENCES "+ MESSAGE_TABLE_NAME+"("+ MESSAGE_COLUMN_MESSAGE_ID+")"
            + MESSAGE_COLUMN_CONTENT+ " TEXT,  "
            + MESSAGE_COLUMN_DATE+ " TEXT, "
            + MESSAGE_COLUMN_TIME + " TEXT ,"
            + MESSAGE_COLUMN_USERTYPE+ " TEXT,"
            + " FOREIGN KEY ("+ MESSAGE_COLUMN_USERTYPE +") REFERENCES "+ USERTYPE_TABLE_NAME+"("+ USERTYPE_COLUMN_ID+")";

    public VolutimeDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {
            // SQL statement Creates table VOLUNTEERS
            db.execSQL(CREATE_TABLE_VOLUNTEER);

        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            // SQL statement to create ORGANIZATION table

            db.execSQL(CREATE_TABLE_ORGANIZATION);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            // SQL statement to create USERTYPE
            db.execSQL(CREATE_TABLE_USERTYPE);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            // SQL statement to create ORGANIZATIONS OF VOLUNTEER
            db.execSQL(CREATE_TABLE_VOLUNTEER_ORG);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            // SQL statement to create VOLUNTEER ACTIVITY
            db.execSQL(CREATE_TABLE_EVENT);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            // SQL statement to create VOLUNTEER ACTIVITY
            db.execSQL(CREATE_TABLE_MESSAGE);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+VOLUNTEER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ORGANIZATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+EVENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+USERTYPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MESSAGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+VOL_AT_ORG_TABLE_NAME);

        onCreate(db);
    }

    public long addVolunteer(Volunteer volunteer) {
       // Add Volunteer To DB
        long result=-1;
        try {
            // make values to be inserted
            ContentValues values = new ContentValues();
            //add all fields
            values.put(VOLUNTEER_COLUMN_EMAIL, volunteer.getEmail());
            values.put(VOLUNTEER_COLUMN_PASSWORD, volunteer.getPassword());
            values.put(VOLUNTEER_COLUMN_FNAME, volunteer.getfName());
            values.put(VOLUNTEER_COLUMN_LNAME, volunteer.getlName());
            values.put(VOLUNTEER_COLUMN_ADDRESS, volunteer.getAddress());
            values.put(VOLUNTEER_COLUMN_DATEOFBIRTH, volunteer.getBirthDate());

            //turn bitmap to blob
            Bitmap image = volunteer.getProfilePic();
            if (image != null) {
                byte[] data = getBitmapAsByteArray(image);
                if (data != null && data.length > 0) {
                    values.put(VOLUNTEER_COLUMN_VOLUPIC, data);
                }
            }

            // insert item
          result=  db.insert(VOLUNTEER_TABLE_NAME, null, values);


        } catch (Throwable t) {
            t.printStackTrace();
        }

        return result;
    }
   public void addOrganization(Organization org) {
       // Add Organization To DB

       try {
           // make values to be inserted
           ContentValues values = new ContentValues();
           //add all fields

           values.put(ORGANIZATION_COLUMN_NAME, org.getName());
           values.put(ORGANIZATION_COLUMN_ADDRESS, org.getAddress());
           values.put(ORGANIZATION_COLUMN_EMAIL, org.getEmail());
           values.put(ORGANIZATION_COLUMN_PASSWORD, org.getPassword());


           //turn bitmap to blob
           Bitmap image = org.getProfilePic();
           if (image != null) {
               byte[] data = getBitmapAsByteArray(image);
               if (data != null && data.length > 0) {
                   values.put(ORGANIZATION_COLUMN_PIC, data);
               }
           }

           // insert org
           db.insert(ORGANIZATION_TABLE_NAME, null, values);


       } catch (Throwable t) {
           t.printStackTrace();
       }

   }
    public void addEvent(VolEvent event) {
        // Add Activity To DB

        try {
            // make values to be inserted
            ContentValues values = new ContentValues();
            //add all fields
            values.put(EVENT_COLUMN_VOLUNTEER_ID, event.getVolID());
            values.put(EVENT_COLUMN_ORG_ID, event.getOrgID());
            values.put(EVENT_COLUMN_DATE, event.getDate());
            values.put(EVENT_COLUMN_START_TIME, event.getStartTime());
            values.put(EVENT_COLUMN_END_TIME, event.getEndTime());
            values.put(EVENT_COLUMN_DETAILS, event.getDetails());

            // insert org
            db.insert(EVENT_TABLE_NAME, null, values);


        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void addOrgToVolunteer(int volID ,int orgID, String startDate, String endDate) {
        // Add Organization To volunteer

        try {
            // make values to be inserted
            ContentValues values = new ContentValues();
            //add all fields
            values.put(VOL_AT_ORG_COLUMN_VOLUNTEER_ID, volID);
            values.put(VOL_AT_ORG_COLUMN_ORG_ID, orgID);
            values.put(VOL_AT_ORG_COLUMN_START_DATE, startDate);
            values.put(VOL_AT_ORG_COLUMN_END_DATE, endDate);


            // insert org
            db.insert(VOL_AT_ORG_TABLE_NAME, null, values);


        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    public void addType(String userType) {
        // Add Type To DB

        try {
            // make values to be inserted
            ContentValues values = new ContentValues();
            //add all fields
            values.put(USERTYPE_COLUMN_TYPE, userType);
            // insert org
            db.insert(USERTYPE_TABLE_NAME, null, values);


        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void addMessage(int senderID ,int receiverID,int parentID, String content, String date, String time, int userType) {
        // Add message To DB

        try {
            // make values to be inserted
            ContentValues values = new ContentValues();
            //add all fields
            values.put(MESSAGE_COLUMN_SENDER_ID, senderID);
            values.put(MESSAGE_COLUMN_RECEIVE_ID, receiverID);
            values.put(MESSAGE_COLUMN_PARENT_ID, parentID);
            values.put(MESSAGE_COLUMN_CONTENT, content);
            values.put(MESSAGE_COLUMN_DATE, date);
            values.put(MESSAGE_COLUMN_TIME, time);
            values.put(MESSAGE_COLUMN_USERTYPE, userType);
            // insert org
            db.insert(VOL_AT_ORG_TABLE_NAME, null, values);


        } catch (Throwable t) {
            t.printStackTrace();
        }

    }
    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Volunteer readVolunteer(int id) {
        //select the volunteer
        Volunteer vol = null ;
        Cursor cursor = null;
        try {
            cursor = db
                    .query(VOLUNTEER_TABLE_NAME,
                            TABLE_VOLUNTEER_COLUMNS, VOLUNTEER_COLUMN_ID + " = ?",
                            new String[]{String.valueOf(id)}, null, null,
                            null, null);


            // if results !=null, parse the first one
            if (cursor != null && cursor.getCount() > 0) {

                cursor.moveToFirst();

                vol = new Volunteer();
                vol.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_ID))));
                vol.setfName(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_FNAME)));
                vol.setlName(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_LNAME)));
                vol.setAddress(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_ADDRESS)));
                vol.setBirthDate(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_DATEOFBIRTH)));
                vol.setEmail(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_EMAIL)));
                vol.setPassword(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_PASSWORD)));


                //images
                byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(VOLUNTEER_COLUMN_VOLUPIC));
                if (imgByte != null && imgByte.length > 0) {
                    Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    if (image != null) {
                        vol.setProfilePic(image);
                    }
                }

            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return vol;
    }

    public Volunteer getVolunteerUser(String email, String password) {
        //select the volunteer
        //TODO check again

        Volunteer vol = null ;
        Cursor cursor = null;
        try {
            cursor = db
                    .query(VOLUNTEER_TABLE_NAME,
                            TABLE_VOLUNTEER_COLUMNS, VOLUNTEER_COLUMN_EMAIL + " = ? " +VOLUNTEER_COLUMN_PASSWORD + " = ? ",
                            new String[]{String.valueOf(email),String.valueOf(password) }, null, null,
                            null, null);

            // if results !=null, parse the first one
            if (cursor != null || cursor.getCount() != 0) {

                    cursor.moveToFirst();

                    vol = new Volunteer();
                    vol.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_ID))));
                    vol.setfName(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_FNAME)));
                    vol.setlName(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_LNAME)));
                    vol.setAddress(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_ADDRESS)));
                    vol.setBirthDate(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_DATEOFBIRTH)));
                    vol.setEmail(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_EMAIL)));
                    vol.setPassword(cursor.getString(cursor.getColumnIndex(VOLUNTEER_COLUMN_PASSWORD)));


                    //images
                    byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(VOLUNTEER_COLUMN_VOLUPIC));
                    if (imgByte != null && imgByte.length > 0) {
                        Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                        if (image != null) {
                            vol.setProfilePic(image);
                        }
                    }

                }

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return vol;
    }

    public Organization readOrganization(int id) {
        //select the ORG
        Organization org = null;
        Cursor cursor = null;
        try {
            cursor = db
                    .query(ORGANIZATION_TABLE_NAME,
                            TABLE_ORGANIZATION_COLUMNS, ORGANIZATION_COLUMN_ID + " = ?",
                            new String[]{String.valueOf(id)}, null, null,
                            null, null);


            // if results !=null, parse the first one
            if (cursor != null && cursor.getCount() > 0) {

                cursor.moveToFirst();

                org = new Organization();
                org.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_ID))));
                org.setName(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_NAME)));
                org.setAddress(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_ADDRESS)));
                org.setEmail(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_EMAIL)));
                org.setPassword(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_PASSWORD)));


                //images
                byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(ORGANIZATION_COLUMN_PIC));
                if (imgByte != null && imgByte.length > 0) {
                    Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    if (image != null) {
                        org.setProfilePic(image);
                    }
                }

            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return org;
    }


    public Message readMessage(int id) {

        Message msg = null;
        Cursor cursor = null;
        try {
            cursor = db
                    .query(MESSAGE_TABLE_NAME,
                            TABLE_MESSAGE_COLUMNS, MESSAGE_COLUMN_MESSAGE_ID + " = ?",
                            new String[]{String.valueOf(id)}, null, null,
                            null, null);


            // if results !=null, parse the first one
            if (cursor != null && cursor.getCount() > 0) {

                cursor.moveToFirst();

                msg = new Message();
                msg.setMessageID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_MESSAGE_ID))));
                msg.setSenderID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_RECEIVE_ID))));
                msg.setReceiverID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_SENDER_ID))));
                msg.setParentID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_PARENT_ID))));

                msg.setContent(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_CONTENT)));
                msg.setSendDate(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_DATE)));
                msg.setSendTime(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_EMAIL))));


            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return msg;
    }
    public VolEvent readEvent(int id) {

        VolEvent event = null ;
        Cursor cursor = null;
        try {
            cursor = db
                    .query(EVENT_TABLE_NAME,
                            TABLE_EVENT_COLUMNS, EVENT_COLUMN_ID + " = ?",
                            new String[]{String.valueOf(id)}, null, null,
                            null, null);


            // if results !=null, parse the first one
            if (cursor != null && cursor.getCount() > 0) {

                cursor.moveToFirst();

                event = new VolEvent();
                event.setVolEventID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_ID))));
                event.setVolID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_VOLUNTEER_ID))));
                event.setOrgID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_ORG_ID))));
                event.setDate(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DATE)));
                event.setStartTime(Integer.parseInt(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_START_TIME))));
                event.setEndTime(Integer.parseInt(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_END_TIME))));
                event.setDetails(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DETAILS)));


            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return event;
    }
    public ArrayList<Organization> getAllOrgs() {
        ArrayList<Organization> result = new ArrayList<Organization>();
        Cursor cursor = null;
        try {
            cursor = db.query(ORGANIZATION_TABLE_NAME, TABLE_ORGANIZATION_COLUMNS, null, null,
                    null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Organization org = cursorToOrganization(cursor);
                result.add(org);
                cursor.moveToNext();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            // make sure to close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }

        return result;
    }

    private Organization cursorToOrganization(Cursor cursor) {
        Organization result = new Organization();
        try {
            result.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_ID))));
            result.setName(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_NAME)));
            result.setAddress(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_ADDRESS)));
            result.setEmail(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_EMAIL)));
            result.setPassword(cursor.getString(cursor.getColumnIndex(ORGANIZATION_COLUMN_PASSWORD)));
            //images
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(ORGANIZATION_COLUMN_PIC));
            if (imgByte != null && imgByte.length > 0) {
                Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                if (image != null) {
                    result.setProfilePic(image);
                }
            }


        } catch (Throwable t) {
            t.printStackTrace();
        }

        return result;
    }
    //TODO MORE READ METHODS

    ///////////////////////////////////
    public int updateVolunteer(Volunteer volunteer) {
        int cnt = 0;
        try {

            // make values to be inserted
            ContentValues values = new ContentValues();
            values.put(VOLUNTEER_COLUMN_FNAME, volunteer.getfName());
            values.put(VOLUNTEER_COLUMN_LNAME, volunteer.getlName());
            values.put(VOLUNTEER_COLUMN_ADDRESS, volunteer.getAddress());
            values.put(VOLUNTEER_COLUMN_DATEOFBIRTH, volunteer.getBirthDate());
            values.put(VOLUNTEER_COLUMN_EMAIL, volunteer.getEmail());
            values.put(VOLUNTEER_COLUMN_PASSWORD, volunteer.getPassword());
            //images
            Bitmap image = volunteer.getProfilePic();
            if (image != null) {
                byte[] data = getBitmapAsByteArray(image);
                if (data != null && data.length > 0) {
                    values.put( VOLUNTEER_COLUMN_VOLUPIC, data);
                }
            } else {
                values.putNull(VOLUNTEER_COLUMN_VOLUPIC);
            }


            // update
            cnt = db.update(VOLUNTEER_TABLE_NAME, values, VOLUNTEER_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(volunteer.getId())});
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return cnt;
    }

    public int updateOrg(Organization org) {
        int cnt = 0;
        try {

            // make values to be inserted
            ContentValues values = new ContentValues();
            values.put(ORGANIZATION_COLUMN_NAME, org.getName());
            values.put(ORGANIZATION_COLUMN_ADDRESS, org.getAddress());
            values.put(ORGANIZATION_COLUMN_EMAIL, org.getEmail());
            values.put(ORGANIZATION_COLUMN_PASSWORD, org.getPassword());
            //images
            Bitmap image = org.getProfilePic();
            if (image != null) {
                byte[] data = getBitmapAsByteArray(image);
                if (data != null && data.length > 0) {
                    values.put( ORGANIZATION_COLUMN_PIC, data);
                }
            } else {
                values.putNull(ORGANIZATION_COLUMN_PIC);
            }


            // update
            cnt = db.update(ORGANIZATION_TABLE_NAME, values, ORGANIZATION_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(org.getId())});
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return cnt;
    }
    public ArrayList<Integer> orgOfVol(int userID){
        ArrayList<Integer> orgsInt=new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db
                    .query(VOL_AT_ORG_TABLE_NAME,
                            TABLE_VOL_AT_ORG_COLUMNS, VOL_AT_ORG_COLUMN_VOLUNTEER_ID + " = ? " ,
                            new String[]{String.valueOf(userID) }, null, null,
                            null, null);

            // if results !=null, parse the first one
            if (cursor != null || cursor.getCount() >= 0) {

                cursor.moveToFirst();


                orgsInt.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex(VOL_AT_ORG_COLUMN_ORG_ID))));

            }

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return orgsInt;

    }

    public int updateEvent(VolEvent event) {
        int cnt = 0;
        try {

            // make values to be inserted
            ContentValues values = new ContentValues();
            values.put(EVENT_COLUMN_DATE, event.getDate());
            values.put(EVENT_COLUMN_START_TIME, event.getStartTime());
            values.put(EVENT_COLUMN_END_TIME, event.getEndTime());
            values.put(EVENT_COLUMN_DETAILS, event.getDetails());


            // update
            cnt = db.update(EVENT_TABLE_NAME, values, EVENT_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(event.getVolEventID())});
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return cnt;
    }
    public int updateVolAtOrg(int volID ,int orgID, String startDate, String endDate) {
        int cnt = 0;
       try {

            // make values to be inserted
            ContentValues values = new ContentValues();

            values.put(EVENT_COLUMN_END_TIME,startDate);
            values.put(EVENT_COLUMN_DETAILS, endDate);


            // update
            cnt = db.update(VOL_AT_ORG_TABLE_NAME, values, VOL_AT_ORG_COLUMN_VOLUNTEER_ID + " = ?"
                    + VOL_AT_ORG_COLUMN_ORG_ID +"=?" ,
                    new String[]{String.valueOf(volID), String.valueOf(orgID)});
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return cnt;
    }

    public void deleteEvent(VolEvent event) {

        try {
            // delete item
            db.delete(EVENT_TABLE_NAME, EVENT_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(event.getVolEventID())});
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public void close() {
        try {
            db.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}