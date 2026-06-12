<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Contract extends Model
{
    use HasFactory;

    protected $fillable = [
        'job_listing_id',
        'client_id',
        'freelance_id',
        'proposal_id',
        'amount',
        'status',
        'payment_status',
        'deadline',
        'stripe_payment_id',
        'stripe_transfer_id',
        'client_signed',
        'freelance_signed',
        'client_signed_at',
        'freelance_signed_at',
    ];

    protected $casts = [
        'client_signed'      => 'boolean',
        'freelance_signed'   => 'boolean',
        'client_signed_at'   => 'datetime',
        'freelance_signed_at' => 'datetime',
    ];

    public function jobListing()
    {
        return $this->belongsTo(JobListing::class);
    }

    public function client()
    {
        return $this->belongsTo(User::class, 'client_id');
    }

    public function freelance()
    {
        return $this->belongsTo(User::class, 'freelance_id');
    }

    public function messages()
    {
        return $this->hasMany(Message::class);
    }
}