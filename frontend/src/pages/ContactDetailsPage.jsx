import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function ContactDetailsPage() {
    const [contact, setContact] = useState(null);
    const [error, setError] = useState("");
    const [emails, setEmails] = useState([]);
    const [phones, setPhones] = useState([]);

    const [newEmailAddress, setNewEmailAddress] = useState("");
    const [newEmailLabel, setNewEmailLabel] = useState("");
    const [editingEmailId, setEditingEmailId] = useState(null);
    const [editEmailAddress, setEditEmailAddress] = useState("");
    const [editEmailLabel, setEditEmailLabel] = useState("");

    const [newPhoneNumber, setNewPhoneNumber] = useState("");
    const [newPhoneLabel, setNewPhoneLabel] = useState("");
    const [editingPhoneId, setEditingPhoneId] = useState(null);
    const [editPhoneNumber, setEditPhoneNumber] = useState("");
    const [editPhoneLabel, setEditPhoneLabel] = useState("");

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        async function loadContact() {
            const token = localStorage.getItem("token");

            if (!token) {
                navigate("/");
                return;
            }

            try {
                const response = await fetch("/api/contacts/" + id, {
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                });

                if (!response.ok) {
                    setError("Unable to load contact");
                    return;
                }

                const data = await response.json();
                setContact(data);

                const emailResponse = await fetch(
                    "/api/contact-emails/contact/" + id,
                    {
                        headers: {
                            Authorization: "Bearer " + token,
                        },
                    }
                );

                if (emailResponse.ok) {
                    const emailData = await emailResponse.json();
                    setEmails(emailData);
                }

                const phoneResponse = await fetch(
                    "/api/contact-phones/contact/" + id,
                    {
                        headers: {
                            Authorization: "Bearer " + token,
                        },
                    }
                );

                if (phoneResponse.ok) {
                    const phoneData = await phoneResponse.json();
                    setPhones(phoneData);
                }
            } catch {
                setError("Unable to connect to the server");
            }
        }

        loadContact();
    }, [id, navigate]);

    async function handleAddEmail(event) {
        event.preventDefault();

        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                "/api/contact-emails/contact/" + id,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                    body: JSON.stringify({
                        emailAddress: newEmailAddress,
                        label: newEmailLabel,
                    }),
                }
            );

            if (!response.ok) {
                setError("Unable to add email address");
                return;
            }

            const newEmail = await response.json();

            setEmails([...emails, newEmail]);
            setNewEmailAddress("");
            setNewEmailLabel("");
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    async function handleDeleteEmail(emailId) {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                "/api/contact-emails/" + emailId,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                }
            );

            if (!response.ok) {
                setError("Unable to delete email address");
                return;
            }

            const updatedEmails = emails.filter(
                (email) => email.id !== emailId
            );

            setEmails(updatedEmails);
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    function handleStartEditEmail(email) {
        setEditingEmailId(email.id);
        setEditEmailAddress(email.emailAddress);
        setEditEmailLabel(email.label);
    }

    async function handleSaveEditEmail(emailId) {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                "/api/contact-emails/" + emailId,
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                    body: JSON.stringify({
                        emailAddress: editEmailAddress,
                        label: editEmailLabel,
                    }),
                }
            );

            if (!response.ok) {
                setError("Unable to update email address");
                return;
            }

            const updatedEmail = await response.json();

            const updatedEmails = emails.map((email) =>
                email.id === emailId ? updatedEmail : email
            );

            setEmails(updatedEmails);
            setEditingEmailId(null);
            setEditEmailAddress("");
            setEditEmailLabel("");
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    async function handleAddPhone(event) {
        event.preventDefault();

        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                "/api/contact-phones/contact/" + id,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                    body: JSON.stringify({
                        phoneNumber: newPhoneNumber,
                        label: newPhoneLabel,
                    }),
                }
            );

            if (!response.ok) {
                setError("Unable to add phone number");
                return;
            }

            const newPhone = await response.json();

            setPhones([...phones, newPhone]);

            setNewPhoneNumber("");
            setNewPhoneLabel("");
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    async function handleDeletePhone(phoneId) {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                "/api/contact-phones/" + phoneId,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                }
            );

            if (!response.ok) {
                setError("Unable to delete phone number");
                return;
            }

            const updatedPhones = phones.filter((phone) => phone.id !== phoneId);

            setPhones(updatedPhones);
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    function handleStartEditPhone(phone) {
        setEditingPhoneId(phone.id);
        setEditPhoneNumber(phone.phoneNumber);
        setEditPhoneLabel(phone.label);
    }

    async function handleSaveEditPhone(phoneId) {
        const token = localStorage.getItem("token");

        try {
            const response = await fetch(
                "/api/contact-phones/" + phoneId,
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                    body: JSON.stringify({
                        phoneNumber: editPhoneNumber,
                        label: editPhoneLabel,
                    }),
                }
            );

            if (!response.ok) {
                setError("Unable to update phone number");
                return;
            }

            const updatedPhone = await response.json();

            const updatedPhones = phones.map((phone) =>
                phone.id === phoneId ? updatedPhone : phone
            );

            setPhones(updatedPhones);
            setEditingPhoneId(null);
            setEditPhoneNumber("");
            setEditPhoneLabel("");
            setError("");
        } catch {
            setError("Unable to connect to the server");
        }
    }

    return (
        <div>
            <h1>Contact Details</h1>

            {error && <p>{error}</p>}

            {contact && (
                <div>
                    <h2>
                        {contact.firstName} {contact.lastName}
                    </h2>
                    <p>{contact.title}</p>

                    <h3>Email Addresses</h3>

                    <form onSubmit={handleAddEmail}>
                        <input
                            type="email"
                            placeholder="Email address"
                            value={newEmailAddress}
                            onChange={(event) => setNewEmailAddress(event.target.value)}
                            required
                        />

                        <input
                            type="text"
                            placeholder="Label"
                            value={newEmailLabel}
                            onChange={(event) => setNewEmailLabel(event.target.value)}
                            required
                        />

                        <button type="submit">Add Email</button>
                    </form>

                    {emails.length === 0 && <p>No email addresses added.</p>}

                    {emails.map((email) => (
                        <div key={email.id}>
                            {editingEmailId === email.id ? (
                            <div>
                                <input type="email" value={editEmailAddress} onChange={(event) =>
                                        setEditEmailAddress(event.target.value)
                                    }
                                />

                                <input type="text" value={editEmailLabel} onChange={(event) =>
                                        setEditEmailLabel(event.target.value)
                                    }
                                />

                                <button type="button" onClick={() => handleSaveEditEmail(email.id)}>
                                    Save
                                </button>

                                <button type="button" onClick={() => {
                                        setEditingEmailId(null);
                                        setEditEmailAddress("");
                                        setEditEmailLabel("");
                                    }}
                                >
                                    Cancel
                                </button>
                            </div>
                        ) : (
                            <p>
                                {email.label}: {email.emailAddress}

                                <button type="button" onClick={() => handleStartEditEmail(email)}>
                                    Edit
                                </button>

                                <button type="button" onClick={() => handleDeleteEmail(email.id)}>
                                    Delete
                                </button>
                            </p>
                            )}
                        </div>
                    ))}

                    <h3>Phone Numbers</h3>

                    <form onSubmit={handleAddPhone}>
                        <input
                            type="text"
                            placeholder="Phone number"
                            value={newPhoneNumber}
                            onChange={(event) => setNewPhoneNumber(event.target.value)}
                            required
                        />

                        <input
                            type="text"
                            placeholder="Label"
                            value={newPhoneLabel}
                            onChange={(event) => setNewPhoneLabel(event.target.value)}
                            required
                        />

                        <button type="submit">Add Phone</button>
                    </form>

                    {phones.length === 0 && <p>No phone numbers added.</p>}

                    {phones.map((phone) => (
                        <div key={phone.id}>
                            {editingPhoneId === phone.id ? (
                                <div>
                                    <input
                                        type="text"
                                        value={editPhoneNumber}
                                        onChange={(event) =>
                                        setEditPhoneNumber(event.target.value)
                                        }
                                    />

                                    <input
                                        type="text"
                                        value={editPhoneLabel}
                                        onChange={(event) =>
                                        setEditPhoneLabel(event.target.value)
                                        }
                                    />

                                    <button type="button" onClick={() => handleSaveEditPhone(phone.id)}>
                                        Save
                                    </button>

                                    <button type="button" onClick={() => {
                                        setEditingPhoneId(null);
                                        setEditPhoneNumber("");
                                        setEditPhoneLabel("");
                                    }}
                                    >
                                        Cancel
                                    </button>
                                </div>
                            ) : (
                                <p>
                                    {phone.label}: {phone.phoneNumber}

                                    <button type="button" onClick={() => handleStartEditPhone(phone)}>
                                        Edit
                                    </button>

                                    <button type="button" onClick={() => handleDeletePhone(phone.id)}>
                                        Delete
                                    </button>
                                </p>
                                )}
                            </div>
                        ))} 
                </div>
            )}
        </div>
    );
}

export default ContactDetailsPage;