<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class DbHandler {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    /* ------------- `users` table method ------------------ */

    /**
     * Creating new user
     * @param String $name User full name
     * @param String $email User login email id
     * @param String $password User login password
     */
    public function createUser($name, $email, $password) {
        require_once 'PassHash.php';
        $response = array();

        // First check if user already existed in db
        if (!$this->isUserExists($email)) {
            // Generating password hash
            $password_hash = PassHash::hash($password);

            // Generating API key
            $api_key = $this->generateApiKey();

            // insert query
            $stmt = $this->conn->prepare("INSERT INTO users(name, email, password_hash, api_key, status) values(?, ?, ?, ?, 1)");
            $stmt->bind_param("ssss", $name, $email, $password_hash, $api_key);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                return USER_CREATED_SUCCESSFULLY;
            } else {
                // Failed to create user
                return USER_CREATE_FAILED;
            }
        } else {
            // User with same email already existed in the db
            return USER_ALREADY_EXISTED;
        }

        return $response;
    }

    /**
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return boolean User login status success/fail
     */
    public function checkLogin($email, $password) {
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT password_hash FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->bind_result($password_hash);

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // Found user with the email
            // Now verify the password

            $stmt->fetch();

            $stmt->close();

            if (PassHash::check_password($password_hash, $password)) {
                // User password is correct
                return TRUE;
            } else {
                // user password is incorrect
                return FALSE;
            }
        } else {
            $stmt->close();

            // user not existed with the email
            return FALSE;
        }
    }

    /**
     * Checking for duplicate user by email address
     * @param String $email email to check in db
     * @return boolean
     */
    private function isUserExists($email) {
        $stmt = $this->conn->prepare("SELECT id from users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getUserByEmail($email) {
        $stmt = $this->conn->prepare("SELECT name, email, api_key, status, created_at FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        if ($stmt->execute()) {
            // $user = $stmt->get_result()->fetch_assoc();
            $stmt->bind_result($name, $email, $api_key, $status, $created_at);
            $stmt->fetch();
            $user = array();
            $user["name"] = $name;
            $user["email"] = $email;
            $user["api_key"] = $api_key;
            $user["status"] = $status;
            $user["created_at"] = $created_at;
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching user api key
     * @param String $user_id user id primary key in user table
     */
    public function getApiKeyById($user_id) {
        $stmt = $this->conn->prepare("SELECT api_key FROM users WHERE id = ?");
        $stmt->bind_param("i", $user_id);
        if ($stmt->execute()) {
            // $api_key = $stmt->get_result()->fetch_assoc();
            // TODO
            $stmt->bind_result($api_key);
            $stmt->close();
            return $api_key;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching user id by api key
     * @param String $api_key user api key
     */
    public function getUserId($api_key) {
        $stmt = $this->conn->prepare("SELECT id FROM users WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        if ($stmt->execute()) {
            $stmt->bind_result($user_id);
            $stmt->fetch();
            // TODO
            // $user_id = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user_id;
        } else {
            return NULL;
        }
    }

    /**
     * Validating user api key
     * If the api key is there in db, it is a valid key
     * @param String $api_key user api key
     * @return boolean
     */
    public function isValidApiKey($api_key) {
        $stmt = $this->conn->prepare("SELECT id from users WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Generating random Unique MD5 String for user Api key
     */
    private function generateApiKey() {
        return md5(uniqid(rand(), true));
    }

    /* ------------- `tasks` table method ------------------ */

    /**
     * Creating new task
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function createTask($user_id, $task) {
        $stmt = $this->conn->prepare("INSERT INTO tasks(task) VALUES(?)");
        $stmt->bind_param("s", $task);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            // task row created
            // now assign the task to user
            $new_task_id = $this->conn->insert_id;
            $res = $this->createUserTask($user_id, $new_task_id);
            if ($res) {
                // task created successfully
                return $new_task_id;
            } else {
                // task failed to create
                return NULL;
            }
        } else {
            // task failed to create
            return NULL;
        }
    }

    /**
     * Fetching single task
     * @param String $task_id id of the task
     */
    public function getTask($task_id, $user_id) {
        $stmt = $this->conn->prepare("SELECT t.id, t.task, t.status, t.created_at from tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        if ($stmt->execute()) {
            $res = array();
            $stmt->bind_result($id, $task, $status, $created_at);
            // TODO
            // $task = $stmt->get_result()->fetch_assoc();
            $stmt->fetch();
            $res["id"] = $id;
            $res["task"] = $task;
            $res["status"] = $status;
            $res["created_at"] = $created_at;
            $stmt->close();
            return $res;
        } else {
            return NULL;
        }
    }
    
    public function   getQuotes($quote_id, $auther_id) {
        
        if($quote_id == 'all' && $auther_id == 'all')
            {
            
            
            $usersList_array =array();
            $user_array = array();
            $note_array = array();
    
           
                  $stmt = $this->conn->prepare("SELECT * from auther");
                  $stmt->execute();

                  $result = $stmt->get_result();
                  $stmt->close();
                  
                  
                  while ($row_users = $result->fetch_assoc()) 
        {
   
                      
                      $user_array['id'] = $row_users['id_auther'];
                      $user_array['surnameName'] = $row_users['auther_name'].' '.$row_users['auther_name'];
                      $user_array['notes'] = array();
        
                      $id_auther = $row_users['id_auther'];  

   
                       $stmt1 = $this->conn->prepare("SELECT * from quotes where id_auther = ".$id_auther." ");
                  $stmt1->execute();

                  $result = $stmt1->get_result();
                  $stmt1->close();
                  
    
    
    
    while ($row_notes = $result->fetch_assoc()) {
        $note_array['id_quote']=$row_notes['id_quote'];
        $note_array['quote']=$row_notes['quote'];
        
        array_push($user_array['notes'],$note_array);
    }

    array_push($usersList_array,$user_array);
}

//$jsonData = json_encode($usersList_array, JSON_PRETTY_PRINT);
return $usersList_array; 

            }



            
//                  $stmt = $this->conn->prepare("SELECT t. * , a. * 
//                                                FROM quotes t, auther a
//                                                WHERE t.id_auther = a.id_auther
//                                                ORDER BY a.id_auther");
//        
//        
////        SELECT t . * , a . * 
////                                                FROM quotes t, auther a
////                                                WHERE t.id_auther = a.id_auther
////                                                AND a.id_auther =1
//        
//        $stmt->execute();
//        $result = $stmt->get_result();
//        $stmt->close();
//        
//        
//        $quotes = array();
//
//        while ($quote = $result->fetch_assoc()) {
//    
//            
//            $quotes[] = $quote;
//
//            }
//            $auther = array();
//            $temp_auther = array();
//            $temp_auther['id_auther'] = "";
//            $temp_auther['quotes'] = array();
//            $temp_quote = array();
//
//            foreach ($quotes as $record) {
//                
//               // print_r($record);
//              
//                if(($temp_auther['id_auther']) == $record['id_auther']){
//                    
//                    
//                    $temp_quote['id_quote'] = $record['id_quote'];
//                    $temp_quote['quote'] = $record['quote'];
//                    $temp_quote['quote_likes_count'] = $record['quote_likes_count'];
//                    $temp_quote['quote_created_on'] = $record['quote_created_on'];
//                    
//                    array_push($temp_auther['quotes'], $temp_quote);
//                    
//                }else{
//                    
//
//                                        
//                    $temp_auther['id_auther'] =$record['id_auther'];
//                    $temp_auther['auther_name'] =$record['auther_name'];
//                    $temp_auther['auther_likes_count'] =$record['auther_likes_count'];
//                    $temp_auther['created_on'] =$record['created_on'];
//
//                  
//                    
//                    $temp_quote['id_quote'] = $record['id_quote'];
//                    $temp_quote['quote'] = $record['quote'];
//                    $temp_quote['quote_likes_count'] = $record['quote_likes_count'];
//                    $temp_quote['quote_created_on'] = $record['quote_created_on'];
//                    
//                    array_push($temp_auther['quotes'], $temp_quote);
//
//        
//                    array_push($auther, $temp_auther);
//
//
//                   // echo "no";
//                }
//                
//                
//
//                                        
//
//                
//            }
//
//      return $auther;
        
        
      //  $stmt = $this->conn->prepare("SELECT t.id, t.task, t.status, t.created_at from tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
     //   $stmt->bind_param("ii", $task_id, $user_id);
//        if ($stmt->execute()) {
////            $res = array();
////            $stmt->bind_result($id_quote, $id_auther, $quote, $quote_likes_count, $quote_created_on);
////            // TODO
////            // $task = $stmt->get_result()->fetch_assoc();
////            $stmt->fetch();
////            $res["id_quote"] = $id_quote;
////            $res["id_auther"] = $id_auther;
////            $res["quote"] = $quote;
////            $res["quote_likes_count"] = $quote_likes_count;
////            $res["quote_created_on"] = $quote_created_on;
////            $stmt->close();
////            return $res;
//            
//        $tasks = $stmt->get_result();
//        $stmt->close();
//        return $tasks;
//        
//        
//        } else {
//            return NULL;
//        }
    }
    
                
    
   

    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */ 
    public function getAllUserTasks($user_id) {
        $stmt = $this->conn->prepare("SELECT t.* FROM tasks t, user_tasks ut WHERE t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $tasks = $stmt->get_result();
        $stmt->close();
        
        return $tasks;
    }

    /**
     * Updating task
     * @param String $task_id id of the task
     * @param String $task task text
     * @param String $status task status
     */
    public function updateTask($user_id, $task_id, $task, $status) {
        $stmt = $this->conn->prepare("UPDATE tasks t, user_tasks ut set t.task = ?, t.status = ? WHERE t.id = ? AND t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("siii", $task, $status, $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

    /**
     * Deleting a task
     * @param String $task_id id of the task to delete
     */
    public function deleteTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("DELETE t FROM tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

    /* ------------- `user_tasks` table method ------------------ */

    /**
     * Function to assign a task to user
     * @param String $user_id id of the user
     * @param String $task_id id of the task
     */
    public function createUserTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("INSERT INTO user_tasks(user_id, task_id) values(?, ?)");
        $stmt->bind_param("ii", $user_id, $task_id);
        $result = $stmt->execute();

        if (false === $result) {
            die('execute() failed: ' . htmlspecialchars($stmt->error));
        }
        $stmt->close();
        return $result;
    }

}

?>
