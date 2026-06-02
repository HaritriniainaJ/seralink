<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class User extends Authenticatable {
    use HasApiTokens, HasFactory, Notifiable;

    protected $fillable = [
        'name', 'email', 'password', 'role',
        'phone', 'bio', 'avatar', 'location', 'is_active'
    ];

    protected $hidden = ['password', 'remember_token'];

    protected $casts = [
        'email_verified_at' => 'datetime',
        'password' => 'hashed',
        'is_active' => 'boolean',
    ];

    public function isAdmin(): bool { return $this->role === 'admin'; }
    public function isFreelance(): bool { return $this->role === 'freelance'; }
    public function isClient(): bool { return $this->role === 'client'; }

    public function skills() { return $this->belongsToMany(Skill::class); }
    public function jobListings() { return $this->hasMany(JobListing::class, 'client_id'); }
    public function proposals() { return $this->hasMany(Proposal::class, 'freelance_id'); }
    public function clientContracts() { return $this->hasMany(Contract::class, 'client_id'); }
    public function freelanceContracts() { return $this->hasMany(Contract::class, 'freelance_id'); }
    public function sentMessages() { return $this->hasMany(Message::class, 'sender_id'); }
    public function receivedMessages() { return $this->hasMany(Message::class, 'receiver_id'); }
    public function reviewsGiven() { return $this->hasMany(Review::class, 'reviewer_id'); }
    public function reviewsReceived() { return $this->hasMany(Review::class, 'reviewee_id'); }
}