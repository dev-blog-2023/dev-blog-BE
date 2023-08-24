import React, { useEffect } from 'react';
import axios from 'axios';

function App() {
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios({
          // url: 'http://52.79.222.161:8080/login',
          // url: 'https://52.79.222.161:8080/',
          // url: 'http://localhost:8080/login',
          url: 'http://localhost:8080/home',
          // method: 'POST',
          method: 'GET',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            // 'Authorization': 'bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhIiwiaWF0IjoxNjkyMzY5MzM4LCJleHAiOjE2OTIzNzI5Mzh9.3RaSrOrBpyly1-Oq89eOOgYOGAhPGyfKoEX8AhJOSiXCL2AXM5tBzRWRcZtVhiqYytqZHdrIGRNCiGdw8RuTgQ'
          },
          withCredentials: true,
          // data: 'username=a&password=a'
        });
        console.log(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  return <div>Check the console for request results!</div>;
}

export default App;