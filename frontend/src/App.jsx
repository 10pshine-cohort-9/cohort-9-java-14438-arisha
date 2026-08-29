import { BrowserRouter, Routes, Route } from "react-router-dom";

import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ContactsPage from "./pages/ContactsPage";
import ContactDetailsPage from "./pages/ContactDetailsPage";
import ProfilePage from "./pages/ProfilePage";
import AddContactPage from "./pages/AddContactPage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/contacts" element={<ContactsPage />} />
                <Route path="/contacts/new" element={<AddContactPage />} />
                <Route path="/profile" element={<ProfilePage />} />
                <Route path="/contacts/:id" element={<ContactDetailsPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
