Till now it supports SwiftUI like: 
1. H,V and ZStacks -Same syntax, but you can wrap these in DriftView{} to center align and start modifying as in SwiftUI
2. Paddings Example- Text("hello", Modifier.padding(leading = 20)
3. Background & Color
4. Shapes, ClipShape
5. Frame
6. font modifier - To use this we need to remove default "import androidx.compose.material3.Text" and add "import com.example.driftui.Text" (if you dont modify)
7. Images (the file shoud be saved in - app -> src -> main -> res -> drawable -> Your image )
```
Image("//Image name goes here")

```
8. TextField & SecureField(for passwords)

```
// Defining variables
    val username = remember { State("") }
    val password = remember { State("") }

// Using in TextField and SecureField
    TextField("Username",value = username, Modifier.frame(width= 170, height = 40))
    SecureField("Password", value = password, Modifier.frame(width= 170, height = 40))

// Accessing Variables values:
    Text("Live Username: ${username.value}")
    Text("Live Password: ${password.value}")


```   
9. Buttons
```
Button(
                        action = {
                            username.set("")
                            password.set("")
                        },
                        Modifier
                            .frame(width = 280, height = 50)
                            .clipShape(Capsule()) // Clip before background
                            .background(Color(0xFF567779)) // Using the new Swifty color
                    ) {
                        Text("Clear",
                            Modifier
                                .foregroundStyle(Color.white) // Using the new Swifty color
                                .font(system(size = 26, weight = bold))
                        )
                    }
```




10. Divider()
```
Divider(color = Color.white, thickness = 5)
```

11. Lists 
```
// Simple Implementation:
List(alignment = center, modifier = Modifier.padding(top = 50)){
    Text("Hello World")
    Divider()
    Text("banana")
     }

// Using Arrays:
val food = listOf("Apple", "Banana", "Orange", "Grape")
List(food, alignment = top, modifier = Modifier.padding(top = 50)) { item ->
     Text(item, modifier = Modifier.padding(16))
     }

Or you can also say items = array name as:
List(items = food, alignment = top, modifier = Modifier.padding(top = 50)) { item ->
     Text(item, modifier = Modifier.padding(16))
     }

```

Steps to Use DriftUI:
1. Create a new Module of type Android Library
2. Name it driftui
3. if already not created, create a new package named- "com.example.driftui" and there create a new Kotlin Class/File and paste the contents of Layout.kt from the repo
4. Satisfy build.gradle requirements.
5. And you are ready to go!

Project Structure:
![ss](https://github.com/user-attachments/assets/ce719ab5-02a8-407b-98e2-5cc479a90a9f)

Checkout Screenhot:
![Screenshot 2025-11-17 at 1 28 04 AM](https://github.com/user-attachments/assets/894b674d-a3e8-4b94-8a59-e4aa5ccbf8d4)
