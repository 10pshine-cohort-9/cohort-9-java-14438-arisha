import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function ContactsPage() {
    const [contacts, setContacts] = useState([]);
    const [error, setError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        async function loadContacts() {
            const token = localStorage.getItem("token");

            if (!token) {
                navigate("/");
                return;
            }

            try {
                const response = await fetch("/api/contacts", {
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                });

                if (!response.ok) {
                    setError("Unable to load contacts");
                    return;
                }

                const data = await response.json();
                setContacts(data.content);
            } catch {
                setError("Unable to connect to the server");
            }
        }

        loadContacts();
    }, [navigate]);

    return (
        <div>
            <h1>Contacts</h1>

            {error && <p>{error}</p>}

            {contacts.length === 0 && !error && (
                <p>No contacts found.</p>
            )}

            {contacts.map((contact) => (
                <div key={contact.id}>
                    <h3>
                        {contact.firstName} {contact.lastName}
                    </h3>
                    <p>{contact.title}</p>
                </div>
            ))}
        </div>
    );
}

export default ContactsPage;