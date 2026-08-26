import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function ContactsPage() {
    const [contacts, setContacts] = useState([]);
    const [error, setError] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [title, setTitle] = useState("");
    const [editingId, setEditingId] = useState(null);
    const [editFirstName, setEditFirstName] = useState("");
    const [editLastName, setEditLastName] = useState("");
    const [editTitle, setEditTitle] = useState("");

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

        async function handleCreateContact(event) {
            event.preventDefault();

            const token = localStorage.getItem("token");

            try {
                const response = await fetch("/api/contacts", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                    body: JSON.stringify({
                    firstName: firstName,
                    lastName: lastName,
                    title: title,
                    }),
                });
       
                if (!response.ok) {
                    setError("Unable to create contact");
                    return;
                }

                const newContact = await response.json();

                setContacts([...contacts, newContact]);

                setFirstName("");
                setLastName("");
                setTitle("");
                setError("");
            } catch {
                setError("Unable to connect to the server");
            }
    }
    async function handleDeleteContact(contactId) {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch("/api/contacts/" + contactId, {
                method: "DELETE",
                headers: {
                    Authorization: "Bearer " + token,
                },
            });

            if (!response.ok) {
                setError("Unable to delete contact");
                return;
            }

            const updatedContacts = contacts.filter(
                (contact) => contact.id !== contactId
            );

            setContacts(updatedContacts);
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }  

    function startEditing(contact) {
        setEditingId(contact.id);
        setEditFirstName(contact.firstName);
        setEditLastName(contact.lastName);
        setEditTitle(contact.title || "");
    }

    async function handleUpdateContact(event) {
        event.preventDefault();

        const token = localStorage.getItem("token");

        try {
            const response = await fetch("/api/contacts/" + editingId, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + token,
                },
                body: JSON.stringify({
                    firstName: editFirstName,
                    lastName: editLastName,
                    title: editTitle,
                }),
            });

            if (!response.ok) {
                setError("Unable to update contact");
                return;
            }

            const updatedContact = await response.json();

            const updatedContacts = contacts.map((contact) => {
                if (contact.id === editingId) {
                    return updatedContact;
                }

                 return contact;
            });

            setContacts(updatedContacts);
            setEditingId(null);
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    return (
        <div>
            <h1>Contacts</h1>
            <h2>Add Contact</h2>

            <form onSubmit={handleCreateContact}>
            <div>
                <label>First Name</label>
                <input
                    type="text"
                    value={firstName}
                    onChange={(event) => setFirstName(event.target.value)}
                    required
                />
            </div>

            <div>
                <label>Last Name</label>
                <input
                    type="text"
                    value={lastName}
                    onChange={(event) => setLastName(event.target.value)}
                    required
                />
            </div>

            <div>
                <label>Title</label>
                <input
                    type="text"
                    value={title}
                    onChange={(event) => setTitle(event.target.value)}
                />
            </div>

            <button type="submit">Add Contact</button>
        </form>

            {error && <p>{error}</p>}

            {contacts.length === 0 && !error && (
                <p>No contacts found.</p>
            )}

            {contacts.map((contact) => (
                <div key={contact.id}>
                    {editingId === contact.id ? (
                        <form onSubmit={handleUpdateContact}>
                            <input
                                type="text"
                                value={editFirstName}
                                onChange={(event) => setEditFirstName(event.target.value)}
                                required
                            />

                            <input
                                type="text"
                                value={editLastName}
                                onChange={(event) => setEditLastName(event.target.value)}
                                required
                            />

                            <input
                                type="text"
                                value={editTitle}
                                onChange={(event) => setEditTitle(event.target.value)}
                            />

                            <button type="submit">Save</button>

                            <button
                                type="button"
                                onClick={() => setEditingId(null)}
                            >
                                Cancel
                            </button>
                        </form>
                    ) : (
                        <>
                            <h3>
                                {contact.firstName} {contact.lastName}
                            </h3>

                            <p>{contact.title}</p>

                            <button onClick={() => startEditing(contact)}>
                                Edit
                            </button>

                            <button onClick={() => handleDeleteContact(contact.id)}>
                                Delete
                            </button>
                        </>
                    )}
                </div>
            ))}
        </div>
    );
}

export default ContactsPage;