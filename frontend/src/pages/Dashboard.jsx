import { useState, useEffect } from 'react'
import { createUser, getUser, getSubjectNotes } from '../api/client'

export const USER_KEY = 'noteshub_user_id'

function StatCard({ label, value, icon, color = 'var(--accent-hover)' }) {
  const rgb = color === 'var(--green)' ? '63,185,80' : color === 'var(--purple)' ? '163,113,247' : '31,111,235'
  return (
    <div className="card" style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
      <div style={{ width: 44, height: 44, borderRadius: 10, background: `rgba(${rgb},0.12)`, display: 'flex', alignItems: 'center', justifyContent: 'center', color, flexShrink: 0 }}>
        {icon}
      </div>
      <div>
        <div style={{ fontSize: 24, fontWeight: 700, letterSpacing: '-0.03em', lineHeight: 1 }}>{value}</div>
        <div style={{ fontSize: 12, color: 'var(--text-muted)', marginTop: 2 }}>{label}</div>
      </div>
    </div>
  )
}

export default function Dashboard({ navigate }) {
  const [user, setUser] = useState(null)
  const [subjectNotes, setSubjectNotes] = useState([])
  const [modal, setModal] = useState(null)
  const [tab, setTab] = useState('login')
  const [form, setForm] = useState({ name: '', email: '', password: '', userId: '' })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [init, setInit] = useState(true)

  useEffect(() => {
    const uid = localStorage.getItem('noteshub_user_id')
    if (uid) {
      getUser(uid)
        .then(u => { setUser(u); return getSubjectNotes(uid) })
        .then(setSubjectNotes)
        .catch(() => localStorage.removeItem('noteshub_user_id'))
        .finally(() => setInit(false))
    } else {
      setInit(false)
    }
  }, [])

  const openModal = (startTab) => {
    setTab(startTab)
    setForm({ name: '', email: '', password: '', userId: '' })
    setError('')
    setModal(true)
  }

  const handleRegister = async () => {
    if (!form.name || !form.email || !form.password) { setError('All fields are required'); return }
    setLoading(true); setError('')
    try {
      const u = await createUser({ name: form.name, email: form.email, password: form.password })
      localStorage.setItem('noteshub_user_id', u.id)
      setUser(u)
      setSubjectNotes([])
      setModal(null)
      alert(`Account created! Your User ID for login:\n\n${u.id}\n\nSave this somewhere safe.`)
    } catch (e) { setError(e.message) }
    finally { setLoading(false) }
  }

  const handleLogin = async () => {
    if (!form.userId.trim()) { setError('User ID is required'); return }
    setLoading(true); setError('')
    try {
      const u = await getUser(form.userId.trim())
      localStorage.setItem('noteshub_user_id', u.id)
      setUser(u)
      const sn = await getSubjectNotes(u.id)
      setSubjectNotes(sn)
      setModal(null)
    } catch (e) { setError('User not found. Check your User ID.') }
    finally { setLoading(false) }
  }

  const handleLogout = () => {
    localStorage.removeItem('noteshub_user_id')
    setUser(null)
    setSubjectNotes([])
  }

  if (init) return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
      <div className="spinner" />
    </div>
  )

  return (
    <div className="page">
      <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', marginBottom: 32 }}>
        <div>
          <h1 style={{ fontSize: 28, fontWeight: 700, letterSpacing: '-0.03em' }}>
            {user ? `Welcome back, ${user.name}` : 'Welcome to NotesHub'}
          </h1>
          <p style={{ color: 'var(--text-muted)', marginTop: 6 }}>
            {user ? 'Your git-inspired note management dashboard' : 'Sign in or create an account to get started'}
          </p>
        </div>
        {user ? (
          <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
            <div style={{ textAlign: 'right', marginRight: 4 }}>
              <div style={{ fontSize: 13, fontWeight: 600 }}>{user.name}</div>
              <div style={{ fontSize: 12, color: 'var(--text-muted)' }}>{user.email}</div>
            </div>
            <button className="btn btn-secondary btn-sm" onClick={handleLogout}>Sign out</button>
          </div>
        ) : (
          <div style={{ display: 'flex', gap: 8 }}>
            <button className="btn btn-secondary" onClick={() => openModal('login')}>Sign in</button>
            <button className="btn btn-primary" onClick={() => openModal('register')}>
              <svg width="14" height="14" viewBox="0 0 16 16" fill="currentColor"><path d="M7.75 2a.75.75 0 01.75.75V7h4.25a.75.75 0 010 1.5H8.5v4.25a.75.75 0 01-1.5 0V8.5H2.75a.75.75 0 010-1.5H7V2.75A.75.75 0 017.75 2z"/></svg>
              Create Account
            </button>
          </div>
        )}
      </div>

      {user ? (
        <>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 16, marginBottom: 32 }}>
            <StatCard label="Notebooks" value={subjectNotes.length} color="var(--accent-hover)" icon={<svg width="20" height="20" viewBox="0 0 16 16" fill="currentColor"><path d="M2 2.5A2.5 2.5 0 014.5 0h8.75a.75.75 0 01.75.75v12.5a.75.75 0 01-.75.75h-2.5a.75.75 0 110-1.5h1.75v-2h-8a1 1 0 00-.714 1.7.75.75 0 01-1.072 1.05A2.495 2.495 0 012 11.5v-9zm10.5-1V9h-8c-.356 0-.694.074-1 .208V2.5a1 1 0 011-1h8z"/></svg>}/>
            <StatCard label="Public" value={subjectNotes.filter(s => s.visibility === 'PUBLIC').length} color="var(--green)" icon={<svg width="20" height="20" viewBox="0 0 16 16" fill="currentColor"><path d="M8 0a8 8 0 100 16A8 8 0 008 0zM1.5 8a6.5 6.5 0 1113 0 6.5 6.5 0 01-13 0z"/></svg>}/>
            <StatCard label="Forked" value={subjectNotes.filter(s => s.forkedFromId).length} color="var(--purple)" icon={<svg width="20" height="20" viewBox="0 0 16 16" fill="currentColor"><path d="M5 3.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm0 2.122a2.25 2.25 0 10-1.5 0v.878A2.25 2.25 0 005.75 8.5h1.5v2.128a2.251 2.251 0 101.5 0V8.5h1.5a2.25 2.25 0 002.25-2.25v-.878a2.25 2.25 0 10-1.5 0v.878a.75.75 0 01-.75.75h-4.5A.75.75 0 015 6.25v-.878z"/></svg>}/>
          </div>
          <div style={{ marginBottom: 32 }}>
            <h2 style={{ fontSize: 12, fontWeight: 600, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: 12 }}>Quick Actions</h2>
            <button className="btn btn-secondary" onClick={() => navigate('subject-notes')}>
              <svg width="14" height="14" viewBox="0 0 16 16" fill="currentColor"><path d="M2 2.5A2.5 2.5 0 014.5 0h8.75a.75.75 0 01.75.75v12.5a.75.75 0 01-.75.75h-2.5a.75.75 0 110-1.5h1.75v-2h-8a1 1 0 00-.714 1.7.75.75 0 01-1.072 1.05A2.495 2.495 0 012 11.5v-9zm10.5-1V9h-8c-.356 0-.694.074-1 .208V2.5a1 1 0 011-1h8z"/></svg>
              View My Notebooks
            </button>
          </div>
          {subjectNotes.length > 0 ? (
            <div>
              <h2 style={{ fontSize: 12, fontWeight: 600, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: 12 }}>Recent Notebooks</h2>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                {subjectNotes.slice(0, 5).map(sn => (
                  <div key={sn.id} className="card" style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: 12 }}
                    onClick={() => navigate('notes-list', { subjectNoteId: sn.id, title: sn.title })}>
                    <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor" style={{ color: 'var(--text-muted)', flexShrink: 0 }}><path d="M2 2.5A2.5 2.5 0 014.5 0h8.75a.75.75 0 01.75.75v12.5a.75.75 0 01-.75.75h-2.5a.75.75 0 110-1.5h1.75v-2h-8a1 1 0 00-.714 1.7.75.75 0 01-1.072 1.05A2.495 2.495 0 012 11.5v-9zm10.5-1V9h-8c-.356 0-.694.074-1 .208V2.5a1 1 0 011-1h8z"/></svg>
                    <div style={{ flex: 1, minWidth: 0 }}>
                      <div style={{ fontWeight: 500 }}>{sn.title}</div>
                      {sn.description && <div style={{ fontSize: 12, color: 'var(--text-muted)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{sn.description}</div>}
                    </div>
                    <span className={`badge badge-${sn.visibility === 'PUBLIC' ? 'green' : 'gray'}`}>{sn.visibility.toLowerCase()}</span>
                    <svg width="14" height="14" viewBox="0 0 16 16" fill="currentColor" style={{ color: 'var(--text-subtle)', flexShrink: 0 }}><path d="M6.22 3.22a.75.75 0 011.06 0l4.25 4.25a.75.75 0 010 1.06l-4.25 4.25a.75.75 0 01-1.06-1.06L9.94 8 6.22 4.28a.75.75 0 010-1.06z"/></svg>
                  </div>
                ))}
              </div>
            </div>
          ) : (
            <div className="empty-state">
              <svg width="40" height="40" viewBox="0 0 16 16" fill="currentColor"><path d="M2 2.5A2.5 2.5 0 014.5 0h8.75a.75.75 0 01.75.75v12.5a.75.75 0 01-.75.75h-2.5a.75.75 0 110-1.5h1.75v-2h-8a1 1 0 00-.714 1.7.75.75 0 01-1.072 1.05A2.495 2.495 0 012 11.5v-9zm10.5-1V9h-8c-.356 0-.694.074-1 .208V2.5a1 1 0 011-1h8z"/></svg>
              <h3>No notebooks yet</h3>
              <p>Create your first notebook to start organizing your notes</p>
              <button className="btn btn-primary" onClick={() => navigate('subject-notes')}>Go to Notebooks</button>
            </div>
          )}
        </>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 16, maxWidth: 900 }}>
          {[
            { title: 'Version Control', desc: 'Every save creates a commit. Roll back to any version, just like git.', icon: '⎇' },
            { title: 'Notebooks', desc: 'Organize notes by subject. Fork others\' notebooks to build on them.', icon: '📚' },
            { title: 'Linked Notes', desc: 'Connect related notes with bidirectional links.', icon: '🔗' },
            { title: 'Tags & Sources', desc: 'Tag notes for quick filtering. Attach source references.', icon: '🏷️' },
          ].map(f => (
            <div key={f.title} className="card">
              <div style={{ fontSize: 28, marginBottom: 10 }}>{f.icon}</div>
              <div style={{ fontWeight: 600, marginBottom: 4 }}>{f.title}</div>
              <div style={{ fontSize: 13, color: 'var(--text-muted)' }}>{f.desc}</div>
            </div>
          ))}
        </div>
      )}

      {/* Auth modal */}
      {modal && (
        <div className="modal-backdrop" onClick={() => setModal(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div style={{ display: 'flex', borderBottom: '1px solid var(--border)', marginBottom: 20, marginTop: -4 }}>
              {[['login', 'Sign in'], ['register', 'Create account']].map(([val, label]) => (
                <button key={val} onClick={() => { setTab(val); setError('') }} style={{
                  background: 'none', border: 'none', padding: '8px 16px',
                  borderBottom: tab === val ? '2px solid var(--accent-hover)' : '2px solid transparent',
                  color: tab === val ? 'var(--accent-hover)' : 'var(--text-muted)',
                  cursor: 'pointer', fontFamily: 'var(--font)', fontSize: 14,
                  fontWeight: tab === val ? 600 : 400, marginBottom: -1,
                }}>
                  {label}
                </button>
              ))}
            </div>

            {tab === 'register' ? (
              <>
                <div className="form-group">
                  <label className="input-label">Username</label>
                  <input className="input" placeholder="your_username" value={form.name} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} autoFocus />
                </div>
                <div className="form-group">
                  <label className="input-label">Email</label>
                  <input className="input" type="email" placeholder="you@example.com" value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} />
                </div>
                <div className="form-group">
                  <label className="input-label">Password</label>
                  <input className="input" type="password" placeholder="••••••••" value={form.password} onChange={e => setForm(f => ({ ...f, password: e.target.value }))} onKeyDown={e => e.key === 'Enter' && handleRegister()} />
                </div>
                {error && <div style={{ color: 'var(--red)', fontSize: 13, marginBottom: 8 }}>{error}</div>}
                <div className="modal-footer">
                  <button className="btn btn-secondary" onClick={() => setModal(null)}>Cancel</button>
                  <button className="btn btn-primary" onClick={handleRegister} disabled={loading}>{loading ? 'Creating…' : 'Create Account'}</button>
                </div>
              </>
            ) : (
              <>
                <div style={{ background: 'var(--orange-subtle)', border: '1px solid rgba(210,153,34,0.3)', borderRadius: 6, padding: '10px 14px', marginBottom: 16, fontSize: 13, color: 'var(--orange)' }}>
                  Paste your <strong>User ID</strong> (UUID shown after account creation) to sign back in.
                </div>
                <div className="form-group">
                  <label className="input-label">User ID</label>
                  <input className="input" placeholder="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx" value={form.userId} onChange={e => setForm(f => ({ ...f, userId: e.target.value }))} onKeyDown={e => e.key === 'Enter' && handleLogin()} autoFocus />
                </div>
                {error && <div style={{ color: 'var(--red)', fontSize: 13, marginBottom: 8 }}>{error}</div>}
                <div className="modal-footer">
                  <button className="btn btn-secondary" onClick={() => setModal(null)}>Cancel</button>
                  <button className="btn btn-primary" onClick={handleLogin} disabled={loading}>{loading ? 'Signing in…' : 'Sign In'}</button>
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </div>
  )
}